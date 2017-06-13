package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import com.angular.gerardosuarez.carpoolingapp.mvp.model.Passenger;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.rx.DefaultPresenterObserver;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyQuotaView;

import java.util.ArrayList;
import java.util.List;

public class MyQuotaPresenter {

    private MyQuotaView view;

    public MyQuotaPresenter(MyQuotaView view) {
        this.view = view;
    }

    public void init() {
        view.setAdapterObserver(new MyQuotaObserver(this));
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

    private class MyQuotaObserver extends DefaultPresenterObserver<Integer, MyQuotaPresenter> {

        public MyQuotaObserver(MyQuotaPresenter presenter) {
            super(presenter);
        }

        @Override
        public void onNext(Integer value) {
            super.onNext(value);
            view.remove(value);
        }
    }


}
