package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.view.View;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.activity.MainActivity;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.ActivityView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainView extends ActivityView<MainActivity> {

    @BindView(R.id.bottom_navigation)
    BottomNavigationViewEx bottomMenu;

    public MainView(MainActivity activity) {
        super(activity);
        ButterKnife.bind(this, activity);
        bottomMenu = (BottomNavigationViewEx) activity.findViewById(R.id.bottom_navigation);
    }

    public void init() {

    }

    public void hideMenu() {
        bottomMenu.setVisibility(View.GONE);
    }

    public void showMenu() {
        bottomMenu.setVisibility(View.VISIBLE);
    }
}
