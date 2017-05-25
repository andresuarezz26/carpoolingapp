package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.activity.AuthActivity;
import com.angular.gerardosuarez.carpoolingapp.activity.MainActivity;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.User;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.rx.DefaultPresenterConsumer;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.AuthView;
import com.angular.gerardosuarez.carpoolingapp.service.AuthUserService;
import com.angular.gerardosuarez.carpoolingapp.utils.Validator;

public class AuthPresenter {

    private AuthView view;
    private AuthUserService service;

    public AuthPresenter(AuthUserService service, AuthView view) {
        this.view = view;
        this.service = service;
    }

    public void init() {
        service.addOnAuthResultConsumer(new AuthResultConsumer(this));
    }

    public void loginUser(String username, String password) {
        if (!Validator.getInstance().stringNotNull(username)) {
            view.showErrorMessage(R.string.error_username_empry);
            return;
        }

        if (!Validator.getInstance().stringNotNull(password)) {
            view.showErrorMessage(R.string.error_password_empry);
            return;
        }

        service.authUser(username, password);
    }

    private void isLoginRight(Boolean result) {
        if (result) {
            showMain();
        } else {
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
