package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyProfileView;

public class MyProfilePresenter {

    private MyProfileView view;
    private MapPreference mapPreference;

    public MyProfilePresenter(MyProfileView view, MapPreference mapPreference) {
        this.view = view;
        this.mapPreference = mapPreference;
    }

    public void init() {
        setCommunity();
        view.hideMenu();
    }

    private void setCommunity() {
        String community = mapPreference.getCommunity();
        view.setCommunityText(community);
    }

    public void hideMenu() {
        view.hideMenu();
    }

    public void showMenu() {
        view.showMenu();
    }
}
