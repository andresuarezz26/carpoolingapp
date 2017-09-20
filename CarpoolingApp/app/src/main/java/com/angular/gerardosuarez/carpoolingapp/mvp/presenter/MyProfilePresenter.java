package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.BaseFragmentPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.User;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyProfileView;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;
import com.angular.gerardosuarez.carpoolingapp.utils.StringUtils;

public class MyProfilePresenter extends BaseFragmentPresenter {

    private MyProfileView view;
    private MapPreference mapPreference;
    private UserService userService;

    public MyProfilePresenter(MyProfileView view, MapPreference mapPreference, UserService userService) {
        super(mapPreference, view, userService);
        this.view = view;
    }

    public void init() {
        setCommunity();
        view.hideMenu();
    }

    private void setCommunity() {
        if (!areAllMapPreferencesNonnull()) {
            String name = getCurrentUserName();
            if (!TextUtils.isEmpty(name)) {
                setCurrentRouteTexts(name, mapPreference.getDate(), StringUtils.EMPTY_STRING, mapPreference.getHour());
            } else {
                setCurrentRouteEmptyTexts();
            }
        } else {
            setCurrentRouteEmptyTexts();
        }
    }

    private String getCurrentUserName() {
        String username = StringUtils.EMPTY_STRING;
        User user = getCurrentUser();
        if (user != null) {
            if (user.name != null) {
                username = user.name;
            }
        }
        return username;
    }

    private void setCurrentRouteTexts(@NonNull String name, @NonNull String date, @NonNull String address, @NonNull String hour) {
        view.setTextName(name);
        view.setTextDate(date);
        view.setTextHour(hour);
        view.setTextAddress(address);
    }

    private void setCurrentRouteEmptyTexts() {
        view.setTextName(StringUtils.EMPTY_STRING);
        view.setTextDate(StringUtils.EMPTY_STRING);
        view.setTextHour(StringUtils.EMPTY_STRING);
        view.setTextAddress(StringUtils.EMPTY_STRING);
    }

    public void hideMenu() {
        view.hideMenu();
    }

    public void showMenu() {
        view.showMenu();
    }

}
