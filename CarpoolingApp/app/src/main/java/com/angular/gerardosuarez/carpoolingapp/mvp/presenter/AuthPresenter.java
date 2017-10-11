package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.activity.AuthActivity;
import com.angular.gerardosuarez.carpoolingapp.activity.MainActivity;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.User;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.AuthView;
import com.angular.gerardosuarez.carpoolingapp.service.AuthUserService;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;
import com.angular.gerardosuarez.carpoolingapp.utils.StringUtils;
import com.angular.gerardosuarez.carpoolingapp.utils.Validator;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import timber.log.Timber;

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

    }

    public void createOrUpdateAuxUser(@NonNull final FirebaseUser firebaseUser) {
        final User user = userService.mapFirebaseUserToUser(firebaseUser);
        createAuxUserNode(user);
    }

    public void createUserIfItDoesntExist(final FirebaseUser firebaseUser) {
        final String newPhotoUrl = getPhotoUrl(firebaseUser);
        userService.getUserByUid(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    userService.createOrUpdateUser(userService.mapFirebaseUserToUserWithImage(firebaseUser, newPhotoUrl));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Nullable
    private String getPhotoUrl(final FirebaseUser user) {
        String facebookUserId = StringUtils.EMPTY_STRING;
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                if (FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                    facebookUserId = profile.getUid();
                }
            }
            return "https://graph.facebook.com/" + facebookUserId + "/picture?height=200";
        } else {
            return facebookUserId;
        }
    }

    private void createAuxUserNode(@NonNull final User user) {
        if (!TextUtils.isEmpty(user.name) && !TextUtils.isEmpty(user.getKey())) {
            userService.createAuxUserNode(user.name + " " + user.getKey());
        }
    }

    public void loginUser(final String username, final String password) {
        AuthActivity activity = view.getActivity();
        if (activity == null) {
            return;
        }

        if (!Validator.stringNotNull(username)) {
            view.showErrorMessage(R.string.error_username_empry);
            return;
        }

        if (!Validator.stringNotNull(password)) {
            view.showErrorMessage(R.string.error_password_empry);
            return;
        }

        service.authUserWithEmail(username, password);
    }

    public void onCompleteLogin(Task<AuthResult> task) {
        Timber.d("signInWithEmail:onComplete:" + task.isSuccessful());
        if (!task.isSuccessful()) {
            view.showErrorMessage(R.string.error_login_invalid);
        }
    }

    public void showMain() {
        final AuthActivity activity = view.getActivity();
        if (activity == null) {
            return;
        }
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }

    public boolean isUserActive(FirebaseUser user) {
        boolean result = false;
        if (user != null) {
            showMain();
            result = true;
        } else {
            Timber.d("User is signed out");
        }
        return result;
    }
}
