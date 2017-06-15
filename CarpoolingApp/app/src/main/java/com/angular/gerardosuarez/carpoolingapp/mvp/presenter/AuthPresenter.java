package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.activity.AuthActivity;
import com.angular.gerardosuarez.carpoolingapp.activity.MainActivity;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.User;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.rx.DefaultPresenterConsumer;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.AuthView;
import com.angular.gerardosuarez.carpoolingapp.service.AuthUserService;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;
import com.angular.gerardosuarez.carpoolingapp.utils.Validator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AuthPresenter {

    private AuthView view;
    private AuthUserService service;
    private UserService userService;

    public AuthPresenter(AuthUserService service, AuthView view, UserService userService) {
        this.view = view;
        this.service = service;
        this.userService = userService;
    }

    public void init() {
        service.addOnAuthResultConsumer(new AuthResultConsumer(this));
    }

    public void loginUser(final String username, final String password) {
        Activity activity = view.getActivity();
        if (activity == null) {
            return;
        }

        if (!Validator.getInstance().stringNotNull(username)) {
            view.showErrorMessage(R.string.error_username_empry);
            return;
        }

        if (!Validator.getInstance().stringNotNull(password)) {
            view.showErrorMessage(R.string.error_password_empry);
            return;
        }

        service.authUser(username, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    for (UserInfo profile : task.getResult().getUser().getProviderData()) {
                        String providerId = profile.getProviderId();
                        String uid = profile.getUid();
                        String name = profile.getDisplayName();
                        String email = profile.getEmail();
                        Uri photoUri = profile.getPhotoUrl();
                        Log.d("fisache", providerId + " " + uid + " " + name + " " + email + " " + photoUri);
                    }
                    processLogin(task.getResult().getUser(), task.getResult().getUser().getProviderData().get(1));
                } else {
                    createAccount(username, password);
                }
            }
        });
    }

    protected void createAccount(String email, String password) {
        service.createUserWithEmail(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            processLogin(task.getResult().getUser(), task.getResult().getUser().getProviderData().get(1));
                        } else {
                            //show login fail
                        }
                    }
                });
    }

    private void processLogin(FirebaseUser firebaseUser, UserInfo userInfo) {
        final User user = User.newInstance(firebaseUser, userInfo);
        userService.getUser(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User remoteUser = dataSnapshot.getValue(User.class);
                        if (remoteUser == null || remoteUser.getUsername() == null) {
                            //activity.showInsertUsername(user);
                        } else {
                            //activity.showLoginSuccess(remoteUser);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //activity.showLoginFail();
                    }
                }
        );
    }

    private void isLoginRight(Boolean result) {
        if (result) {
            showMain();
        } else {
            view.showErrorMessage(R.string.error_login_invalid);
        }
    }

    protected void showMain() {
        final AuthActivity activity = view.getActivity();
        if (activity == null) {
            return;
        }
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }


    private class AuthResultConsumer extends DefaultPresenterConsumer<Boolean, AuthPresenter> {

        private AuthResultConsumer(@NonNull AuthPresenter presenter) {
            super(presenter);
        }

        @Override
        public void accept(Boolean result) throws Exception {
            isLoginRight(result);
        }
    }
}
