package com.angular.gerardosuarez.carpoolingapp.navigation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.text.TextUtils;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.data.preference.role.RolePreference;
import com.angular.gerardosuarez.carpoolingapp.fragment.CommunityChooserFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyBookingDriverFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyBookingPassengerFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyMapFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyProfileFragment;
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
                goToMyProfileFragmentWithoutBackStack();
            } else {
                gotToCommunityChooserFragmentWithotBackStack();
            }
        } else {
            goToRegisterFragment();
        }
    }

    private void goToRegisterFragment() {
        hideMapFragment();
        open(new RegisterFragment(), RegisterFragment.TAG);
    }

    public void goToMyProfileFragment() {
        popEveryFragment();
        open(new MyProfileFragment(), MyProfileFragment.TAG);
    }

    private void gotToCommunityChooserFragmentWithotBackStack() {
        open(new CommunityChooserFragment(), CommunityChooserFragment.TAG);
    }

    private void goToMyProfileFragmentWithoutBackStack() {
        popEveryFragment();
        hideMapFragment();
        openWithoutBackStack(new MyProfileFragment());
    }

    public void goToMapFragment() {
        String role = rolePreference.getCurrentRole();
        if (StringUtils.isEmpty(role)) {
            return;
        }
        open(new MyMapFragment(), MyMapFragment.TAG);
        //openMapFragment(new MyMapFragment(), MyMapFragment.TAG);
    }

    public void goToCommunityChooserFragment() {
        open(new CommunityChooserFragment(), CommunityChooserFragment.TAG);
    }

    public void goToMyBookingsFragment() {
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
        if (fragmentManager == null) return;
        int backStackCount = fragmentManager.getBackStackEntryCount();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.main_container);
        if (currentFragment != null) {
            if (!currentFragment.getClass().equals(fragment.getClass())) {
                addToBackStack(fragment, fragment.getClass().getName());
            }
        } else {
            addToBackStack(fragment, fragment.getClass().getName());
        }
    }

    private void addToBackStack(Fragment fragment, String tag) {
        boolean addBackStack = isThisFragmentOnNoBackStackGroup(tag);
        //boolean popFragment = fragmentManager.popBackStackImmediate(tag, 0);
        //if (!popFragment) {
        fragmentManager
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();
        //}
    }

    private boolean isThisFragmentOnNoBackStackGroup(String tag) {

        return !CommunityChooserFragment.class.getName().equalsIgnoreCase(tag)
                && !RegisterFragment.class.getName().equalsIgnoreCase(tag);
    }

    private void openWithoutBackStack(Fragment fragment) {
        if (fragmentManager != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .commit();
        }
    }

    private void hideMapFragment() {
        /*FragmentTransaction transaction = fragmentManager.beginTransaction();
        MyMapFragment mapFragment = (MyMapFragment) fragmentManager.findFragmentByTag(MyMapFragment.TAG);
        if (mapFragment != null) {
            transaction.hide(mapFragment);
        }
        transaction.commit();*/
    }

    private void popEveryFragment() {
        int backStackCount = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            String backStackId = fragmentManager.getBackStackEntryAt(i).getName();
            fragmentManager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void setToPassengerRole() {
        boolean isANewRole = changeRoleLogic(rolePreference.getCurrentRole(), ROLE_PASSEGNER);
        rolePreference.putCurrentRole(ROLE_PASSEGNER);
        MyMapFragment fragment = getDriverMapFragment();
        if (fragment != null) {
            fragment.onRoleClicked(isANewRole);
        }
    }

    public void setToDriverRole() {
        boolean isANewRole = changeRoleLogic(rolePreference.getCurrentRole(), ROLE_DRIVER);
        rolePreference.putCurrentRole(ROLE_DRIVER);
        MyMapFragment fragment = getDriverMapFragment();
        if (fragment != null) {
            fragment.onRoleClicked(isANewRole);
        }
    }

    private boolean changeRoleLogic(String lastRole, String currentRole) {
        if (!TextUtils.isEmpty(rolePreference.getCurrentRole())) {
            if (!lastRole.equalsIgnoreCase(currentRole)) {
                mapPreference.putAlreadyDataChoosen(false);
                mapPreference.putTime(null);
                mapPreference.putFromOrTo(MapPreference.FROM);
                mapPreference.putDate(null);
                return true;
            }
        }
        return false;
    }
}
