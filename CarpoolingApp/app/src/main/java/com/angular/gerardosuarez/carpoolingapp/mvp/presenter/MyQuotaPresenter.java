package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import com.angular.gerardosuarez.carpoolingapp.mvp.model.Passenger;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyQuotaView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MyQuotaPresenter {

    private MyQuotaView view;

    public MyQuotaPresenter(MyQuotaView view) {
        this.view = view;
    }

    public void init() {
        view.init(mockPassengers());
    }

    private void addAll() {
        view.addAll(mockPassengers());
    }

    private List<Passenger> mockPassengers() {
        Passenger p1 = new Passenger();
        p1.setAddress("Address1");
        p1.setName("Name1");
        Passenger p2 = new Passenger();
        p2.setAddress("Address1");
        p2.setName("Name1");
        List<Passenger> passengers = new ArrayList<>();
        passengers.add(p1);
        passengers.add(p2);
        return passengers;

    }

    private class MyQuotaObserver implements Observer<Passenger> {


        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Passenger passenger) {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }


}
