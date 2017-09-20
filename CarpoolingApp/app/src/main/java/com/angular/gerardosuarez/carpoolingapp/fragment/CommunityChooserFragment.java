package com.angular.gerardosuarez.carpoolingapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.activity.MainActivity;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.fragment.base.BaseMapPreferenceFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.CommunityChooserPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.CommunityChooserView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommunityChooserFragment extends BaseMapPreferenceFragment {

    private CommunityChooserPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community_chooser, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new CommunityChooserPresenter(
                new CommunityChooserView(this),
                mapPreference
        );
        presenter.init();

    }

    @OnClick(R.id.btn_icesi)
    void onIcesiClick() {
        presenter.saveCommunity(MapPreference.COMMUNITY_ICESI);
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null) return;
        if (activity.getNavigationManager() == null) return;
        activity.getNavigationManager().goToMyProfileFragment();
    }

    @OnClick(R.id.btn_javeriana)
    void onJaverianaClick() {
        presenter.saveCommunity(MapPreference.COMMUNITY_JAVERIANA);
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null) return;
        if (activity.getNavigationManager() == null) return;
        activity.getNavigationManager().goToMyProfileFragment();
    }


}
