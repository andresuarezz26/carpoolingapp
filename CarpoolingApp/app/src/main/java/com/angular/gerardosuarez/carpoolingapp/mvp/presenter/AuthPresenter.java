package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import android.content.Intent;

import com.angular.gerardosuarez.carpoolingapp.activity.AuthActivity;
import com.angular.gerardosuarez.carpoolingapp.activity.MainActivity;
import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.mvp.event.OnLoginClickedEvent;
import com.angular.gerardosuarez.carpoolingapp.mvp.event.OnLoginEvent;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.AuthModel;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.AuthView;
import com.angular.gerardosuarez.carpoolingapp.utils.Validator;
import com.squareup.otto.Subscribe;

/**
 * Created by gerardosuarez on 2/03/17.
 */
public class AuthPresenter {

    private AuthModel model;
    private AuthView view;

    public AuthPresenter(AuthModel model, AuthView view) {
        this.model = model;
        this.view = view;
    }

    @Subscribe
    public void onLoginClicked (OnLoginClickedEvent event){

        String username = event.getUsername();

        if (!Validator.getInstance().stringNotNull(username)){
            view.showErrorMessage(R.string.error_username_empry);
            return;
        }

        String password = event.getPassword();

        if (!Validator.getInstance().stringNotNull(password)){
            view.showErrorMessage(R.string.error_password_empry);
            return;
        }

        model.authUser(username, password);

        }

    @Subscribe
    public void onLoginEvent (OnLoginEvent event) {
        if (event.isSuccess()){
            showMain();
            return;
        }else{
            view.showErrorMessage(R.string.error_login_invalid);
        }
    }

    public void showMain (){
       final AuthActivity activity = (AuthActivity) view.getActivity();
        if (activity == null){
            return;
        }

        activity.startActivity(new Intent(activity, MainActivity.class));
    }
}
