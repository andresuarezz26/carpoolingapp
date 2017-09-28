package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import com.angular.gerardosuarez.carpoolingapp.data.preference.init.InitPreference;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.TermsAndConditionView;

public class TermsAndConditionPresenter {
    private TermsAndConditionView view;
    private InitPreference initPreference;

    public TermsAndConditionPresenter(InitPreference initPreference, TermsAndConditionView view) {
        this.initPreference = initPreference;
        this.view = view;
    }

    public void init() {
        view.init();
    }

    public void setTermsAndConditionAccepted() {
        initPreference.putWasTermsAndCondictionsAccepted(true);
    }
}
