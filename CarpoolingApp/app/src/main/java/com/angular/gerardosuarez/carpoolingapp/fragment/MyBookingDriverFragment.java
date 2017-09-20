package com.angular.gerardosuarez.carpoolingapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.fragment.base.BaseMapPreferenceFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.MyBookingDriverFragmentPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyBookingDriverView;
import com.angular.gerardosuarez.carpoolingapp.service.MyBookingDriverService;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyBookingDriverFragment extends BaseMapPreferenceFragment {

    private MyBookingDriverFragmentPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_booking_driver, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new MyBookingDriverFragmentPresenter(
                new MyBookingDriverView(this),
                new MyBookingDriverService(),
                new UserService(),
                mapPreference);
        presenter.init();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getRequestsOfDriver();
    }

    @OnClick(R.id.btn_start_travel)
    void onStartTravelClick() {
        presenter.onStartTravel();
    }

    @OnClick(R.id.btn_cancel_route)
    void onCancelBooking() {
        presenter.onCancelRoute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unsubscribeFirebaseListener();
    }
}
