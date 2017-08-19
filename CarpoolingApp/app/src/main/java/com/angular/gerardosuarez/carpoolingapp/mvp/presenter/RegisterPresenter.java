package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import android.text.TextUtils;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.BaseFragmentPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.User;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.RegisterView;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;


public class RegisterPresenter extends BaseFragmentPresenter {

    private RegisterView view;
    private UserService userService;

    public RegisterPresenter(MapPreference mapPreference, RegisterView view, UserService userService) {
        super(mapPreference, view, userService);
        this.view = view;
        this.userService = userService;
    }


    public void init() {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            view.setInitialTexts(currentUser.name, currentUser.email);
        }
        hideMenu();
    }

    public boolean saveUserData() {
        User user = view.createUserFromForm();
        if (validatePhoneNumber(user)) {
            User userFromFirebase = getCurrentUser();
            if (userFromFirebase != null) {
                user.photo_uri = userFromFirebase.photo_uri;
            }
            if (getMyUid() != null) {
                user.setKey(getMyUid());
                userService.createOrUpdateUser(user);
                mapPreference.putAlreadyRegister(true);
                return true;
            } else {
                mapPreference.putAlreadyRegister(false);
                return false;
            }
        } else {
            return false;
        }
    }


    private boolean validatePhoneNumber(User user) {
        if (TextUtils.isEmpty(user.phone)) {
            view.showToast(R.string.error_empty_fields);
            return false;
        } else {
            if (user.phone.length() != 10) {
                view.showToast(R.string.error_phone_number_characters);
                return false;
            }
        }
        return true;
    }

    public void hideMenu() {
        view.hideMenu();
    }
}
