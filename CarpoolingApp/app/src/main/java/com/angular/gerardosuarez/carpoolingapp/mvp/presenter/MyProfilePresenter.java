package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.BaseFragmentPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.User;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyProfileView;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;
import com.angular.gerardosuarez.carpoolingapp.utils.StringUtils;

public class MyProfilePresenter extends BaseFragmentPresenter {

    private MyProfileView view;

    public MyProfilePresenter(MyProfileView view, MapPreference mapPreference, UserService userService) {
        super(mapPreference, view, userService);
        this.view = view;
    }

    public void init() {
        setCurrentRoute();
        setCommunity();
        view.hideMenu();
    }

    private void setCurrentRoute() {
        if (areAllMapPreferenceNonnull()) {
            validateUserAndSetRouteTexts();
        } else {
            setCurrentRouteEmptyTexts();
        }
    }

    private void validateUserAndSetRouteTexts() {
        validateUserAndSetRouteTexts(
                StringUtils.changeNullByEmpty(mapPreference.getDate()),
                StringUtils.EMPTY_STRING,
                StringUtils.changeNullByEmpty(mapPreference.getHour()));
    }


    private void setCommunity() {
        String community = mapPreference.getCommunity();
        view.setCommunityText(community);
    }

    @NonNull
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

    @NonNull
    private String getImageUrl() {
        String imageUrl = StringUtils.EMPTY_STRING;
        User user = getCurrentUser();
        if (user != null) {
            if (user.name != null) {
                imageUrl = user.photo_uri;
            }
        }
        return imageUrl;
    }

    private void validateUserAndSetRouteTexts(@Nullable String date, @Nullable String address, @Nullable String hour) {
        setUsernameText();
        view.setTextDate(StringUtils.formatDateWithTodayLogic(StringUtils.changeNullByEmpty(date)));
        view.setTextHour(StringUtils.formatHour(StringUtils.changeNullByEmpty(hour)));
        view.setTextAddress(StringUtils.changeNullByEmpty(address));
        view.setImagePhoto(getImageUrl());
    }

    private void setCurrentRouteEmptyTexts() {
        setUsernameText();
        view.setTextDate(StringUtils.EMPTY_STRING);
        view.setTextHour(StringUtils.EMPTY_STRING);
        view.setTextAddress(StringUtils.EMPTY_STRING);
        view.setImagePhoto(getImageUrl());
    }

    private void setUsernameText() {
        view.setTextName(getCurrentUserName());
    }

    public void hideMenu() {
        view.hideMenu();
    }

    public void showMenu() {
        view.showMenu();
    }

}
