package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.widget.Toast;

import com.angular.gerardosuarez.carpoolingapp.activity.AuthActivity;

import butterknife.ButterKnife;


public class AuthView extends ActivityView<AuthActivity> {

    public AuthView(AuthActivity activity) {
        super(activity);
        ButterKnife.bind(this, activity);
    }

    public void showErrorMessage(int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_LONG).show();
    }
}
