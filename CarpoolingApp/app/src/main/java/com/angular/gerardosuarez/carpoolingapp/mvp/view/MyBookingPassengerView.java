package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.adapter.DriverInfoRequestAdapter;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyBookingPassengerFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.FragmentView;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.DriverInfoRequest;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyBookingPassengerView extends FragmentView<MyBookingPassengerFragment, Integer> {
    @BindView(R.id.recycler_view_my_booking_passenger) RecyclerView recyclerView;
    private DriverInfoRequestAdapter adapter;

    public MyBookingPassengerView(MyBookingPassengerFragment fragment) {
        super(fragment);
        if (fragment.getView() != null)
            ButterKnife.bind(this, fragment.getView());
    }

    public void init() {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        adapter = new DriverInfoRequestAdapter(adapterObserver);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);
    }

    public void addAll(List<DriverInfoRequest> list) {
        adapter.addAll(list);
    }

    public void removeAll() {
        adapter.removeAll();
    }

    public void remove(int position) {
        adapter.remove(position);
    }

    public void add(DriverInfoRequest driverInfoRequest) {
        adapter.add(driverInfoRequest);
    }
}
