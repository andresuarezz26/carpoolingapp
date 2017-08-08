package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.CommunityChooserView;

/**
 * Created by gerardosuarez on 7/08/17.
 */

public class CommunityChooserPresenter {

    private CommunityChooserView view;
    private MapPreference mapPreference;

    public CommunityChooserPresenter(CommunityChooserView view, MapPreference mapPreference) {
        this.view = view;
        this.mapPreference = mapPreference;
    }

    public void saveCommunity(String community) {
        mapPreference.putCommunity(community);
    }

    public void init() {
        view.hideMenu();
    }
}
