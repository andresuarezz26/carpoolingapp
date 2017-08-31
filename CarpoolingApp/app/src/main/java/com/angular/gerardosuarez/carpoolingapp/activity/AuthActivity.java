package com.angular.gerardosuarez.carpoolingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.AuthPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.AuthView;
import com.angular.gerardosuarez.carpoolingapp.service.AuthUserService;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//facebook imports

public class AuthActivity extends BaseActivity implements OnCompleteListener<AuthResult> {

    public static final String TAG = AuthActivity.class.getSimpleName();

    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.edit_username)
    EditText editUsername;
    @BindView(R.id.loginButton)
    LoginButton mLoginButton;

    private AuthPresenter presenter;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        mCallbackManager = CallbackManager.Factory.create();

        if (presenter == null) {
            firebaseAuth = FirebaseAuth.getInstance();
            presenter = new AuthPresenter(
                    new AuthUserService(firebaseAuth),
                    new AuthView(this),
                    new UserService()
            );
        }

        presenter.init();


        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFbAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(AuthActivity.this, R.string.loginfb_cancel, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(AuthActivity.this, R.string.loginfb_error, Toast.LENGTH_SHORT).show();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.d(TAG, "user:" + user);
                if (user != null) {
                    presenter.showMain();
                }
            }
        };
    }

    private void handleFbAccessToken(AccessToken accessToken) {
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.loginfirebase_error, Toast.LENGTH_LONG).show();
                } else {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    Log.d(TAG, "user:" + user);
                    if (user != null) {
                        presenter.createOrUpdateUser(user);
                        presenter.showMain();
                    }
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.button_login)
    void onLoginCLick() {
        final String username = editUsername.getText().toString();
        final String password = editPassword.getText().toString();
        presenter.loginUser(username, password);
    }

    @Override
    public void onComplete(@NonNull final Task<AuthResult> task) {
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
