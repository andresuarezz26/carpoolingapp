package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.angular.gerardosuarez.carpoolingapp.activity.AuthActivity;
import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.mvp.event.OnLoginClickedEvent;
import com.squareup.otto.Bus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gerardosuarez
 */
public class AuthView extends ActivityView{

    @BindView(R.id.edit_password) EditText editPassword;
    @BindView(R.id.edit_username) EditText editUsername;

    private Bus bus;

    public AuthView(AuthActivity activity, Bus bus) {
        super(activity);
        ButterKnife.bind(this, activity);
        this.bus = bus;
    }

    @OnClick(R.id.button_login)
    public void onLoginCLicked(View view)
    {
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        bus.post(new OnLoginClickedEvent(username, password));
    }

    public void showErrorMessage(int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_LONG).show();
    }
}
