package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import android.content.Intent;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.activity.AuthActivity;
import com.angular.gerardosuarez.carpoolingapp.activity.MainActivity;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.AuthView;
import com.angular.gerardosuarez.carpoolingapp.service.AuthUserService;
import com.angular.gerardosuarez.carpoolingapp.utils.Validator;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import timber.log.Timber;

public class AuthPresenter {

    private AuthView view;
    private AuthUserService service;

    public AuthPresenter(AuthUserService service, AuthView view) {
        this.view = view;
        this.service = service;
    }

    public void init() {

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

    private void showMain() {
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
