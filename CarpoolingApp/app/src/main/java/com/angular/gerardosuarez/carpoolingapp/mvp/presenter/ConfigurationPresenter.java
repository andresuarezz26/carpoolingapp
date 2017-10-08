package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import com.angular.gerardosuarez.carpoolingapp.data.preference.init.InitPreference;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreferenceDriverImpl;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreferencePassengerImpl;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.BaseFragmentPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.ConfigurationView;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;
import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;

public class ConfigurationPresenter extends BaseFragmentPresenter {
    private ConfigurationView view;
    private FirebaseAuth firebaseAuth;
    private InitPreference initPreference;

    public ConfigurationPresenter(ConfigurationView view, UserService userService, InitPreference initPreference) {
        super(view, userService);
        this.view = view;
        this.initPreference = initPreference;
    }

    public void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        view.init();
    }

    public void onCloseSession() {
        MapPreference driverMapPreference = new MapPreferenceDriverImpl(view.getActivity(), MapPreferenceDriverImpl.NAME);
        resetAllMapPreferences(driverMapPreference);
        MapPreference passengerMapPreference = new MapPreferencePassengerImpl(view.getActivity(), MapPreferencePassengerImpl.NAME);
        resetAllMapPreferences(passengerMapPreference);
        resetInitPreferences();
        firebaseAuth.signOut();
        AccessToken.setCurrentAccessToken(null);
        view.finishActivity();
    }

    private void resetInitPreferences() {
        initPreference.putWasTermsAndCondictionsAccepted(false);
        initPreference.putAlreadyRegistered(false);
        initPreference.putCommunityChoosed(false);
    }

    public void goToReportIssueWebPage() {
        view.goToReportIssueWebPage();
    }

    public void goToReportUserWebPage() {
        view.goToReportUserWebPage();
    }


    public void goToActivateUser() {
        view.goToActiveUserWebPage();
    }
}
