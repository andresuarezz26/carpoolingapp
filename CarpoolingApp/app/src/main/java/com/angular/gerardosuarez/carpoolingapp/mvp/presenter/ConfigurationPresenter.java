package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.BaseFragmentPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.ConfigurationView;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;
import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;

public class ConfigurationPresenter extends BaseFragmentPresenter {
    private ConfigurationView view;
    private FirebaseAuth firebaseAuth;

    public ConfigurationPresenter(MapPreference mapPreference, ConfigurationView view, UserService userService) {
        super(mapPreference, view, userService);
        this.view = view;
    }

    public void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        view.init();
    }

    public void setTermsAndConditionAccepted() {
        mapPreference.putTermsAndConditionAccepted(true);
    }

    public void onCloseSession() {
        resetAllMapPreferences();
        firebaseAuth.signOut();
        AccessToken.setCurrentAccessToken(null);
        view.finishActivity();

    }

    public void goToReportIssueWebPage() {
        view.goToReportIssueWebPage();
    }
}
