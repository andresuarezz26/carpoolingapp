package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyProfileView;

public class MyProfilePresenter {

    private MyProfileView view;

    public MyProfilePresenter(MyProfileView view) {
        this.view = view;
    }

    public void init() {

    }

    public void goToDriverMap() {
        view.goToDriverMap();
    }
}
