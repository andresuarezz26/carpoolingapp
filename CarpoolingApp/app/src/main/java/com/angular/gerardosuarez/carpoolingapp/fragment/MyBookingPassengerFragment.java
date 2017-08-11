package com.angular.gerardosuarez.carpoolingapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreferenceImpl;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.MyBookingPassengerPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyBookingPassengerView;
import com.angular.gerardosuarez.carpoolingapp.service.MyBookingPassengerService;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;

import butterknife.ButterKnife;

public class MyBookingPassengerFragment extends Fragment {

    public static final String TAG = "my_booking_passenger";
    private MyBookingPassengerPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_booking_passenger, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new MyBookingPassengerPresenter(
                new MyBookingPassengerView(this),
                new MyBookingPassengerService(),
                new UserService(),
                new MapPreferenceImpl(getActivity(), MapPreferenceImpl.NAME));
        presenter.init();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getDriversRequestInfo();
    }
}
