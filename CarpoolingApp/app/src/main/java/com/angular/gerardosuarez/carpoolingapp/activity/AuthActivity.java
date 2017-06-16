package com.angular.gerardosuarez.carpoolingapp.activity;

import android.app.Activity;
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

public class AuthActivity extends BaseActivity implements OnCompleteListener<AuthResult> {

    @BindView(R.id.edit_password) EditText editPassword;
    @BindView(R.id.edit_username) EditText editUsername;
    private AuthPresenter presenter;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Activity activity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (presenter == null) {
            firebaseAuth = FirebaseAuth.getInstance();
            presenter = new AuthPresenter(
                    new AuthUserService(firebaseAuth),
                    new AuthView(this),
                    new UserService());
        }
        presenter.init();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (!presenter.isUserActive(user)) {
                    setContentView(R.layout.activity_auth);
                    ButterKnife.bind(activity);
                }
            }
        };
    }

    @OnClick(R.id.button_login)
    void onLoginCLick() {
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        presenter.loginUser(username, password);
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        presenter.onCompleteLogin(task);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
