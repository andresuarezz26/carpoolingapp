package com.angular.gerardosuarez.carpoolingapp.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.AuthPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.AuthView;
import com.angular.gerardosuarez.carpoolingapp.service.AuthUserService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends BaseActivity {

    @BindView(R.id.edit_password) EditText editPassword;
    @BindView(R.id.edit_username) EditText editUsername;
    AuthPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (presenter == null) {
            presenter = new AuthPresenter(new AuthUserService(), new AuthView(this));
        }
        presenter.init();
    }

    @OnClick(R.id.button_login)
    void onLoginCLick() {
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        presenter.loginUser(username, password);
    }
}
