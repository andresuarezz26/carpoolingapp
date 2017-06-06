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
import com.angular.gerardosuarez.carpoolingapp.fragment.MyQuotaFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.base.ActivityView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainView extends ActivityView<MainActivity> {


    private static final String DRIVER_MAP = "DRIVER_MAP";
    private static final String MY_PROFILE = "MY_PROFILE";
    private static final String MY_QUOTA = "MY_QUOTA";

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomMenu;
    private FragmentManager fragmentManager;

    public MainView(MainActivity activity) {
        super(activity);
        ButterKnife.bind(this, activity);
    }

    public void init() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        fragmentManager = activity.getFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, new MyProfileFragment(), MY_PROFILE);
        transaction.commit();
    }

    public void goToDriverMapFragment() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        fragmentManager = activity.getFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        DriverMapFragment mapFragment = (DriverMapFragment) fragmentManager.findFragmentByTag(DRIVER_MAP);
        if (mapFragment == null) {
            transaction.add(R.id.main_container, new DriverMapFragment(), DRIVER_MAP);
        } else {
            transaction.show(mapFragment);
        }
        transaction.commit();
    }

    public void goToMyProfileFragment() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        fragmentManager = activity.getFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_container, new MyProfileFragment(), MY_PROFILE);
        DriverMapFragment mapFragment = (DriverMapFragment) fragmentManager.findFragmentByTag(DRIVER_MAP);
        if (mapFragment != null) {
            transaction.hide(mapFragment);
        }
        transaction.commit();
    }

    public void goToMyQuotaFragment() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        fragmentManager = activity.getFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_container, new MyQuotaFragment(), MY_QUOTA);
        DriverMapFragment mapFragment = (DriverMapFragment) fragmentManager.findFragmentByTag(DRIVER_MAP);
        if (mapFragment != null) {
            transaction.hide(mapFragment);
        }
        transaction.commit();
    }

    public void hideMenu() {
        bottomMenu.setVisibility(View.GONE);
    }

    public void showMenu() {
        bottomMenu.setVisibility(View.VISIBLE);
    }
}
