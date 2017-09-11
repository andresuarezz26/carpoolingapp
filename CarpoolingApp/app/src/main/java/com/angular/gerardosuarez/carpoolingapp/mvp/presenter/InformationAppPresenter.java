package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.InformationAppView;

public class InformationAppPresenter {
    private InformationAppView view;
    private MapPreference mapPreference;

    public InformationAppPresenter(MapPreference mapPreference, InformationAppView view) {
        this.mapPreference = mapPreference;
        this.view = view;
    }

    public void init() {
        view.init();
    }

    public void setTermsAndConditionAccepted() {
        mapPreference.putTermsAndConditionAccepted(true);
    }
}
