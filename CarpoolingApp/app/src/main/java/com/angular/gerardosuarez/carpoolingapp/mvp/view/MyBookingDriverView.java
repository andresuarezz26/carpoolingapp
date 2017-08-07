package com.angular.gerardosuarez.carpoolingapp.mvp.view;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.angular.gerardosuarez.carpoolingapp.R;
import com.angular.gerardosuarez.carpoolingapp.adapter.PassengerInfoRequestAdapter;
import com.angular.gerardosuarez.carpoolingapp.fragment.MyBookingDriverFragment;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.FragmentView;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerInfoRequest;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyBookingDriverView extends FragmentView<MyBookingDriverFragment, Integer> {
    @BindView(R.id.recycler_view_my_booking_driver) RecyclerView recyclerView;
    private PassengerInfoRequestAdapter adapter;

    public MyBookingDriverView(MyBookingDriverFragment fragment) {
        super(fragment);
        ButterKnife.bind(this, fragment.getView());
    }

    public void init() {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        adapter = new PassengerInfoRequestAdapter(adapterObserver);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);
    }


    public void addAll(List<PassengerInfoRequest> list) {
        adapter.addAll(list);
    }

    public void removeAll(){
        adapter.removeAll();
    }

    public void remove(int position) {
        adapter.remove(position);
    }

    public void add(PassengerInfoRequest passengerInfoRequest){
        adapter.add(passengerInfoRequest);
    }
}
