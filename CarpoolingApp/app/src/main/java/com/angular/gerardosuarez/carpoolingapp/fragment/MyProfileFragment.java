package com.angular.gerardosuarez.carpoolingapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.activity.MainActivity;
import com.angular.gerardosuarez.carpoolingapp.fragment.base.BaseMapPreferenceFragment;
import com.angular.gerardosuarez.carpoolingapp.fragment.base.OnPageSelectedListener;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.MyProfilePresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyProfileView;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyProfileFragment extends BaseMapPreferenceFragment implements OnPageSelectedListener {

    private MyProfilePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new MyProfilePresenter(new MyProfileView(this),
                mapPreference,
                new UserService());
        presenter.init();
    }

    @OnClick(R.id.btn_driver)
    void onDriverClick() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null) {
            return;
        }
        presenter.showMenu();
        activity.getNavigationManager().setToDriverRole();
        activity.getNavigationManager().goToMapFragment();
    }

    @OnClick(R.id.btn_passenger)
    void onPassengerClick() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null) {
            return;
        }
        presenter.showMenu();
        activity.getNavigationManager().setToPassengerRole();
        activity.getNavigationManager().goToMapFragment();
    }

    @OnClick(R.id.btn_community)
    void onCommunityChooserClick() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null) {
            return;
        }
        activity.getNavigationManager().goToCommunityChooserFragment();
    }

    @Override
    public void onPageSelected() {
        if (presenter != null) {
            presenter.hideMenu();
        }
    }
}
