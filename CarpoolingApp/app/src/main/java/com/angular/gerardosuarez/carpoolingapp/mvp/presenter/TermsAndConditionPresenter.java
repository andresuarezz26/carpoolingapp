package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.TermsAndConditionView;

public class TermsAndConditionPresenter {
    private TermsAndConditionView view;
    private MapPreference mapPreference;

    public TermsAndConditionPresenter(MapPreference mapPreference, TermsAndConditionView view) {
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
