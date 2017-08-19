package com.angular.gerardosuarez.carpoolingapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.activity.MainActivity;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreferenceImpl;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.MyProfilePresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyProfileView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyProfileFragment extends Fragment {

    public static final String TAG = "my_profile";
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
        presenter = new MyProfilePresenter(new MyProfileView(this), new MapPreferenceImpl(getActivity(), MapPreferenceImpl.NAME));
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
}
