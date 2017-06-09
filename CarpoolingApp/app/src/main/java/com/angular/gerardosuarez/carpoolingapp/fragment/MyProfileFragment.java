package com.angular.gerardosuarez.carpoolingapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.MyProfilePresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyProfileView;
import com.angular.gerardosuarez.carpoolingapp.navigation.NavigationManager;

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
        presenter = new MyProfilePresenter(new MyProfileView(this));
        presenter.init();
    }

    @OnClick(R.id.btn_driver)
    void onDriverClick() {
        NavigationManager.getInstance(getFragmentManager()).goToDriverMapFragment();
        presenter.showMenu();
    }

    @OnClick(R.id.btn_passenger)
    void onPassengerClick() {

    }
}
