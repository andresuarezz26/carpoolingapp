package com.angular.gerardosuarez.carpoolingapp.navigation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.data.preference.role.RolePreference;
import com.angular.gerardosuarez.carpoolingapp.fragment.CommunityChooserFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyBookingPassengerFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyMapFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyProfileFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyBookingDriverFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.RegisterFragment;
import com.angular.gerardosuarez.carpoolingapp.utils.StringUtils;

public class NavigationManager {

    private FragmentManager fragmentManager;
    private RolePreference rolePreference;
    private MapPreference mapPreference;

    private final static String ROLE_DRIVER = "driver";
    private final static String ROLE_PASSEGNER = "passenger";

    public NavigationManager(FragmentManager fragmentManager, RolePreference rolePreference, MapPreference mapPreference) {
        this.fragmentManager = fragmentManager;
        this.rolePreference = rolePreference;
        this.mapPreference = mapPreference;
    }

    public MyMapFragment getDriverMapFragment() {
        return (MyMapFragment) fragmentManager.findFragmentByTag(MyMapFragment.TAG);
    }

    public void chooseInitialScreen() {
        if (mapPreference.isAlreadyRegister()) {
            if (mapPreference.getCommunity() != null) {
                goToMyProfileFragment();
            } else {
                goToCommunityChooserFragment();
            }
        } else {
            goToRegisterFragment();
        }
    }

    private void goToRegisterFragment() {
        popEveryFragment();
        hideMapFragment();
        open(new RegisterFragment(), RegisterFragment.TAG);
    }

    public void goToMyProfileFragment() {
        popEveryFragment();
        hideMapFragment();
        open(new MyProfileFragment(), MyProfileFragment.TAG);
    }

    public void goToMapFragment() {
        popEveryFragment();
        String role = rolePreference.getCurrentRole();
        if (StringUtils.isEmpty(role)) {
            return;
        }
        openMapFragment(new MyMapFragment(), MyMapFragment.TAG);
    }

    public void goToCommunityChooserFragment() {
        popEveryFragment();
        open(new CommunityChooserFragment(), CommunityChooserFragment.TAG);
    }

    public void goToMyBookingsFragment() {
        popEveryFragment();
        hideMapFragment();
        String role = rolePreference.getCurrentRole();
        if (StringUtils.isEmpty(role)) {
            return;
        }
        if (ROLE_DRIVER.equalsIgnoreCase(role)) {
            open(new MyBookingDriverFragment(), MyBookingDriverFragment.TAG);
        } else {
            open(new MyBookingPassengerFragment(), MyBookingPassengerFragment.TAG);
        }
    }

    private void openMapFragment(Fragment fragment, String tag) {
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
        rolePreference.putCurrentRole(ROLE_PASSEGNER);
        MyMapFragment fragment = getDriverMapFragment();
        if (fragment != null) {
            fragment.onRoleChanged();
        }
    }

    public void setToDriverRole() {
        rolePreference.putCurrentRole(ROLE_DRIVER);
        MyMapFragment fragment = getDriverMapFragment();
        if (fragment != null) {
            fragment.onRoleChanged();
        }
    }
}
