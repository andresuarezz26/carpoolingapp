package com.angular.gerardosuarez.carpoolingapp.activity;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.AuthPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.AuthView;
import com.angular.gerardosuarez.carpoolingapp.service.AuthUserService;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class AuthActivity extends BaseActivity {

    @BindView(R.id.edit_password) EditText editPassword;
    @BindView(R.id.edit_username) EditText editUsername;
    private AuthPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (presenter == null) {
            presenter = new AuthPresenter(new AuthUserService(this), new AuthView(this), new UserService(new Application()));
        }
        presenter.init();
    }

    @OnClick(R.id.button_login)
    void onLoginCLick() {
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        //FIXME: Delete this when the login is defined
        username = "tester@tester.com";
        password = "tester";
        presenter.loginUser(username, password);
    }
}
