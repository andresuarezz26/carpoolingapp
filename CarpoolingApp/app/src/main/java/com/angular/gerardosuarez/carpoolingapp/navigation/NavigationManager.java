package com.angular.gerardosuarez.carpoolingapp.navigation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.data.preference.RolePreference;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyMapFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyProfileFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyQuotaFragment;
import com.angular.gerardosuarez.carpoolingapp.utils.StringUtils;

public class NavigationManager {

    private static NavigationManager navigationManager;
    private FragmentManager fragmentManager;
    private RolePreference preference;

    private final static String ROLE_DRIVER = "driver";
    private final static String ROLE_PASSEGNER = "passenger";

    public static NavigationManager getInstance(FragmentManager fragmentManager, RolePreference preference) {
        if (navigationManager == null) {
            navigationManager = new NavigationManager(fragmentManager, preference);
        }
        return navigationManager;
    }

    private NavigationManager(FragmentManager fragmentManager, RolePreference preference) {
        this.fragmentManager = fragmentManager;
        this.preference = preference;
    }

    public MyMapFragment getDriverMapFragment() {
        return (MyMapFragment) fragmentManager.findFragmentByTag(MyMapFragment.TAG);
    }

    public void goToMyProfileFragment() {
        popEveryFragment();
        hideMapFragment();
        open(new MyProfileFragment(), MyProfileFragment.TAG);
    }

    public void goToMapFragment() {
        popEveryFragment();
        String role = preference.getCurrentRole();
        if (StringUtils.isEmpty(role)) {
            return;
        }
        setMapFragment(new MyMapFragment(preference.getCurrentRole()), MyMapFragment.TAG);
    }

    public void goToMyQuotaFragment() {
        popEveryFragment();
        hideMapFragment();
        open(new MyQuotaFragment(), MyQuotaFragment.TAG);
    }

    private void setMapFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        MyMapFragment mapFragment = (MyMapFragment) fragmentManager.findFragmentByTag(MyMapFragment.TAG);
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
        MyMapFragment mapFragment = (MyMapFragment) fragmentManager.findFragmentByTag(MyMapFragment.TAG);
        if (mapFragment != null) {
            transaction.hide(mapFragment);
        }
        transaction.commit();
    }

    private void popEveryFragment() {
        int backStackCount = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            String backStackId = fragmentManager.getBackStackEntryAt(i).getName();
            if (!MyMapFragment.TAG.equals(backStackId)) {
                fragmentManager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }

    public void setToPassengerRole() {
        preference.putCurrentRole(ROLE_PASSEGNER);
    }

    public void setToDriverRole() {
        preference.putCurrentRole(ROLE_DRIVER);
    }

    public void destroyNavigation() {
        navigationManager = null;
    }

}
