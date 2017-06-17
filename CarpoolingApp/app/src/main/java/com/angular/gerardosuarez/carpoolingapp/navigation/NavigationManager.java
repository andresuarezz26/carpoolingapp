package com.angular.gerardosuarez.carpoolingapp.navigation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.fragment.DriverMapFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyProfileFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyQuotaFragment;

public class NavigationManager {

    private static NavigationManager navigationManager;
    private FragmentManager fragmentManager;

    public static NavigationManager getInstance(FragmentManager fragmentManager) {
        if (navigationManager == null) {
            navigationManager = new NavigationManager(fragmentManager);
        }
        return navigationManager;
    }

    private NavigationManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void goToMyProfileFragment() {
        popEveryFragment();
        hideMapFragment();
        open(new MyProfileFragment(), MyProfileFragment.TAG);
    }

    public void goToDriverMapFragment() {
        popEveryFragment();
        setMapFragment(new DriverMapFragment(), DriverMapFragment.TAG);
    }

    public void goToMyQuotaFragment() {
        popEveryFragment();
        hideMapFragment();
        open(new MyQuotaFragment(), MyQuotaFragment.TAG);
    }

    private void setMapFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        DriverMapFragment mapFragment = (DriverMapFragment) fragmentManager.findFragmentByTag(DriverMapFragment.TAG);
        if (mapFragment != null) {
            transaction.show(mapFragment);
        } else {
            transaction.add(R.id.main_container, fragment, tag);
        }
        transaction.commit();
    }

    private void open(Fragment fragment, String tag) {
        if (fragmentManager != null) {
            fragmentManager.beginTransaction()
                    .add(R.id.main_container, fragment)
                    .addToBackStack(tag)
                    .commit();
        }
    }

    private void hideMapFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        DriverMapFragment mapFragment = (DriverMapFragment) fragmentManager.findFragmentByTag(DriverMapFragment.TAG);
        if (mapFragment != null) {
            transaction.hide(mapFragment);
        }
        transaction.commit();
    }

    private void popEveryFragment() {
        int backStackCount = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            String backStackId = fragmentManager.getBackStackEntryAt(i).getName();
            if (!DriverMapFragment.TAG.equals(backStackId)) {
                fragmentManager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }

    public void destroyNavigation() {
        navigationManager = null;
    }

}
