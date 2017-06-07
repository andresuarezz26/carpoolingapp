package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.BottomNavigationView;
import android.view.View;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.activity.MainActivity;
import com.angular.gerardosuarez.carpoolingapp.fragment.DriverMapFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyProfileFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.base.FragmentView;

import butterknife.ButterKnife;

public class MyProfileView extends FragmentView<MyProfileFragment> {

    FragmentManager fragmentManager;
    BottomNavigationView bottomMenu;

    public MyProfileView(MyProfileFragment fragment) {
        super(fragment);
        if (fragment.getView() != null) {
            ButterKnife.bind(this, fragment.getView());
            bottomMenu = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        }
    }

    public void goToDriverMap() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        fragmentManager = activity.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        DriverMapFragment mapFragment = (DriverMapFragment) fragmentManager.findFragmentByTag(MainActivity.DRIVER_MAP_FRAGMENT);
        if (mapFragment != null) {
            transaction.show(mapFragment);
        } else {
            transaction.add(R.id.main_container, new DriverMapFragment(), MainActivity.DRIVER_MAP_FRAGMENT);
        }
        transaction.commit();
    }

    public void showMenu() {
        bottomMenu.setVisibility(View.VISIBLE);
    }
}
