package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.app.Activity;
import android.app.Fragment;
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

import static com.angular.gerardosuarez.carpoolingapp.activity.MainActivity.DRIVER_MAP_FRAGMENT;

public class MainView extends ActivityView<MainActivity> {


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
        transaction.add(R.id.main_container, new MyProfileFragment(), MainActivity.MY_PROFILE_FRAGMENT);
        transaction.commit();
    }

    public void goToDriverMapFragment() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        fragmentManager = activity.getFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        DriverMapFragment mapFragment = (DriverMapFragment) fragmentManager.findFragmentByTag(DRIVER_MAP_FRAGMENT);
        if (mapFragment == null) {
            transaction.add(R.id.main_container, new DriverMapFragment(), DRIVER_MAP_FRAGMENT);
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
        MyProfileFragment myProfileFragment = (MyProfileFragment) fragmentManager.findFragmentByTag(MyProfileFragment.TAG);
        MyQuotaFragment quotaFragment = new MyQuotaFragment();
        DriverMapFragment mapFragment = (DriverMapFragment) fragmentManager.findFragmentByTag(DriverMapFragment.TAG);
        performTransaction(quotaFragment, myProfileFragment, mapFragment, transaction);
        transaction.commit();
    }

    public void goToMyQuotaFragment() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        fragmentManager = activity.getFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        MyProfileFragment myProfileFragment = (MyProfileFragment) fragmentManager.findFragmentByTag(MyProfileFragment.TAG);
        MyQuotaFragment quotaFragment = new MyQuotaFragment();
        DriverMapFragment mapFragment = (DriverMapFragment) fragmentManager.findFragmentByTag(DriverMapFragment.TAG);
        performTransaction(myProfileFragment, quotaFragment, mapFragment, transaction);
        transaction.commit();
    }

    private void performTransaction(Fragment currentFragment, Fragment newFragment, Fragment mapFragment, final FragmentTransaction transaction) {
        if (mapFragment != null) {
            transaction.hide(mapFragment);
            if (currentFragment != null) {
                transaction.remove(currentFragment);
                transaction.add(R.id.main_container, newFragment, newFragment.getTag());
            } else {
                transaction.add(R.id.main_container, newFragment, newFragment.getTag());
            }
        } else {
            transaction.replace(R.id.main_container, newFragment, newFragment.getTag());
        }
    }

    public void hideMenu() {
        bottomMenu.setVisibility(View.GONE);
    }

    public void showMenu() {
        bottomMenu.setVisibility(View.VISIBLE);
    }
}
