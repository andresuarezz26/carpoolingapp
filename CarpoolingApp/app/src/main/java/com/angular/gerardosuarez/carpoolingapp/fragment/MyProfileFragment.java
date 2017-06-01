package com.angular.gerardosuarez.carpoolingapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.MyProfilePresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyProfileView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyProfileFragment extends Fragment {

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
        MyProfilePresenter presenter = new MyProfilePresenter(new MyProfileView(this));
        presenter.init();
    }

    @OnClick(R.id.btn_driver)
    void onDriverClick() {

    }

    @OnClick(R.id.btn_passenger)
    void onPassengerClick() {

    }
}
