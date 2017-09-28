package com.angular.gerardosuarez.carpoolingapp.navigation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.data.preference.init.InitPreference;
import com.angular.gerardosuarez.carpoolingapp.data.preference.role.RolePreference;
import com.angular.gerardosuarez.carpoolingapp.fragment.CommunityChooserFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.ConfigurationFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.InformationAppFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyBookingDriverFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyBookingPassengerFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyMapFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyProfileFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.RegisterFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.TermsAndConditionFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.base.OnPageSelectedListener;
import com.angular.gerardosuarez.carpoolingapp.utils.StringUtils;

import java.util.HashMap;

public class NavigationManager {

    private FragmentManager fragmentManager;
    private RolePreference rolePreference;
    private InitPreference initPreference;


    private final static String ROLE_DRIVER = "driver";
    private final static String ROLE_PASSEGNER = "passenger";

    private HashMap<String, String> isMenuFragmentMap;

    public NavigationManager(@NonNull FragmentManager fragmentManager, @NonNull RolePreference rolePreference, @NonNull InitPreference initPreference) {
        this.fragmentManager = fragmentManager;
        this.rolePreference = rolePreference;
        this.initPreference = initPreference;
        initMenuFragmentHas();
        fragmentManager.addOnBackStackChangedListener(getListener());
    }

    private void initMenuFragmentHas() {
        isMenuFragmentMap = new HashMap<>();
        isMenuFragmentMap.put(MyMapFragment.class.getName(), StringUtils.EMPTY_STRING);
        isMenuFragmentMap.put(MyBookingDriverFragment.class.getName(), StringUtils.EMPTY_STRING);
        isMenuFragmentMap.put(MyBookingPassengerFragment.class.getName(), StringUtils.EMPTY_STRING);
        isMenuFragmentMap.put(InformationAppFragment.class.getName(), StringUtils.EMPTY_STRING);
        isMenuFragmentMap.put(ConfigurationFragment.class.getName(), StringUtils.EMPTY_STRING);
    }

    private FragmentManager.OnBackStackChangedListener getListener() {

        return new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                if (fragmentManager != null) {
                    selectCurrentVisibleFragment();
                }
            }
        };
    }

    private void selectCurrentVisibleFragment() {
        int backStackCount = fragmentManager.getBackStackEntryCount();
        if (backStackCount > 0) {
            int index = backStackCount - 1;
            FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
            String tag = backEntry.getName();
            Fragment fragment = fragmentManager.findFragmentByTag(tag);
            onPageSelectedListener(fragment);
        }
    }

    @Nullable
    public MyMapFragment getMapFragment() {
        Fragment fragment = fragmentManager.findFragmentByTag(MyMapFragment.class.getName());
        if (fragment != null) {
            return (MyMapFragment) fragment;
        }
        return null;
    }

    public void chooseInitialScreen() {
        if (initPreference.wasTermsAndConditionsAccepted()) {
            if (initPreference.isAlreadyRegistered()) {
                if (initPreference.wasComunityChoosed()) {
                    goToMyProfileFragmentWithoutBackStack();
                } else {
                    goToCommunityChooserFragment();
                }
            } else {
                goToRegisterFragment();
            }
        } else {
            goToTermsAndConditionFragment();
        }
    }

    public int getBackStackEntryCount() {
        return fragmentManager.getBackStackEntryCount();
    }

    //region GotoFragments

    private void goToTermsAndConditionFragment() {
        open(new TermsAndConditionFragment());
    }

    private void goToRegisterFragment() {
        open(new RegisterFragment());
    }

    public void goToMyProfileFragment() {
        popEveryFragment();
        open(new MyProfileFragment());
    }

    private void goToMyProfileFragmentWithoutBackStack() {
        popEveryFragment();
        open(new MyProfileFragment());
    }

    public void goToMapFragment() {
        String role = rolePreference.getCurrentRole();
        if (StringUtils.isEmpty(role)) {
            return;
        }
        open(new MyMapFragment());
    }

    public void goToCommunityChooserFragment() {
        open(new CommunityChooserFragment());
    }

    public void goToConfigurationFragment() {
        open(new ConfigurationFragment());
    }

    public void goToMyBookingsFragment() {
        String role = rolePreference.getCurrentRole();
        if (StringUtils.isEmpty(role)) {
            return;
        }
        if (ROLE_DRIVER.equalsIgnoreCase(role)) {
            open(new MyBookingDriverFragment());
        } else {
            open(new MyBookingPassengerFragment());
        }
    }

    public void goToInformatioAppFragment() {
        open(new InformationAppFragment());
    }

    //endregion

    private void open(@NonNull Fragment fragment) {
        if (fragmentManager == null) return;
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.main_container);
        if (currentFragment != null) {
            if (!currentFragment.getClass().equals(fragment.getClass())) {
                performFragmentTransaction(fragment, fragment.getClass().getName());
            }
        } else {
            performFragmentTransaction(fragment, fragment.getClass().getName());
        }
    }

    private void onPageSelectedListener(@NonNull Fragment fragment) {
        if (fragment instanceof OnPageSelectedListener) {
            ((OnPageSelectedListener) fragment).onPageSelected();
        }
    }

    private void performFragmentTransaction(@NonNull Fragment fragment, @NonNull String fragmentClassName) {
        boolean popFragment = fragmentManager.popBackStackImmediate(fragmentClassName, 0);
        if (!popFragment) {
            if (isAMenuFragment(fragment)) {
                fragmentManager
                        .beginTransaction()
                        .add(R.id.main_container, fragment, fragmentClassName)
                        .addToBackStack(fragmentClassName)
                        .commit();
            } else {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.main_container, fragment, fragmentClassName)
                        .addToBackStack(fragmentClassName)
                        .commit();

            }
        }
    }

    private boolean isAMenuFragment(@NonNull Fragment fragment) {
        return isMenuFragmentMap.get(fragment.getClass().getName()) != null;
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
        MyMapFragment fragment = getMapFragment();
        if (fragment != null) {
            fragment.onRoleClicked(isANewRole);
        }
    }

    public void setToDriverRole() {
        boolean isANewRole = changeRoleLogic(rolePreference.getCurrentRole(), ROLE_DRIVER);
        rolePreference.putCurrentRole(ROLE_DRIVER);
        MyMapFragment fragment = getMapFragment();
        if (fragment != null) {
            fragment.onRoleClicked(isANewRole);
        }
    }

    private boolean changeRoleLogic(String lastRole, String currentRole) {
        if (!TextUtils.isEmpty(rolePreference.getCurrentRole())) {
            if (!lastRole.equalsIgnoreCase(currentRole)) {
                return true;
            }
        }
        return false;
    }
}
