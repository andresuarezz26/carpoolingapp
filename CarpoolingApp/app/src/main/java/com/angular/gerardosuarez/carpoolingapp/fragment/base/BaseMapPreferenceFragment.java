package com.angular.gerardosuarez.carpoolingapp.fragment.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreferenceDriverImpl;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreferencePassengerImpl;
import com.angular.gerardosuarez.carpoolingapp.data.preference.role.RolePreference;
import com.angular.gerardosuarez.carpoolingapp.data.preference.role.RolePreferenceImpl;

public class BaseMapPreferenceFragment extends Fragment {
    protected RolePreference rolePreference;
    protected MapPreference mapPreference;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        chooseCurrentMapFragment();
    }

    private void chooseCurrentMapFragment() {
        rolePreference = new RolePreferenceImpl(getActivity(), RolePreferenceImpl.NAME);
        if (RolePreference.ROLE_DRIVER.equalsIgnoreCase(rolePreference.getCurrentRole())) {
            mapPreference = new MapPreferenceDriverImpl(getActivity(), MapPreferenceDriverImpl.NAME);
        } else {
            mapPreference = new MapPreferencePassengerImpl(getActivity(), MapPreferencePassengerImpl.NAME);
        }
    }
}
