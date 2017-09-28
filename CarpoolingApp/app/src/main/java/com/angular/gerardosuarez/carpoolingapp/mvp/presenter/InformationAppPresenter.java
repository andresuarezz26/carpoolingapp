package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import com.angular.gerardosuarez.carpoolingapp.mvp.view.InformationAppView;

public class InformationAppPresenter {
    private InformationAppView view;

    public InformationAppPresenter( InformationAppView view) {
        this.view = view;
    }

    public void init() {
        view.init();
    }

}
