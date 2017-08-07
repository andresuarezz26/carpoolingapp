package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import com.angular.gerardosuarez.carpoolingapp.mvp.base.BasePresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.DriverInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.User;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.rx.DefaultPresenterObserver;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyBookingPassengerView;
import com.angular.gerardosuarez.carpoolingapp.service.MyBookingPassengerService;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MyBookingPassengerPresenter extends BasePresenter {

    private MyBookingPassengerView view;

    private ValueEventListener bookingPassengerListener;
    private ValueEventListener userListener;

    private MyBookingPassengerService bookingPassengerService;
    private UserService userService;

    private List<DriverInfoRequest> driversInfoRequestList;

    public MyBookingPassengerPresenter(MyBookingPassengerView view,
                                       MyBookingPassengerService bookingPassengerService,
                                       UserService userService) {
        this.view = view;
        this.bookingPassengerService = bookingPassengerService;
        this.userService = userService;
    }

    public void init() {
        driversInfoRequestList = new ArrayList<>();
        view.setAdapterObserver(new MyQuotaObserver(this));
        view.init();
    }

    public void unsubscribeFirebaseListener() {
        if (bookingPassengerListener != null) {
            databaseRef.removeEventListener(bookingPassengerListener);
        }
        if (userListener != null) {
            databaseRef.removeEventListener(userListener);
        }
    }

    //MyPassengerDriverService
    public void getDriversRequestInfo() {
        driversInfoRequestList.clear();
        view.removeAll();
        bookingPassengerListener = bookingPassengerService.getPassengerBookings()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            try {
                                DriverInfoRequest driverInfoRequest = snapshot.getValue(DriverInfoRequest.class);
                                if (driverInfoRequest != null) {
                                    if (!DriverInfoRequest.STATUS_CANCELED.equalsIgnoreCase(driverInfoRequest.status)) {
                                        setDriverAditionalInfo(snapshot.getKey(), driverInfoRequest);
                                    }
                                }
                            } catch (DatabaseException e) {
                                Timber.e(e.getMessage(), e);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e(databaseError.toString(), databaseError);
                    }
                });
    }

    private void setDriverAditionalInfo(final String uid, final DriverInfoRequest driverInfoRequest) {
        userService.getUserByUid(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        if (PassengerInfoRequest.STATUS_ACCEPTED.equalsIgnoreCase(driverInfoRequest.status)) {
                            driverInfoRequest.setDriverPhone(user.phone);
                            driverInfoRequest.setDriverEmail(user.email);
                        }
                        driverInfoRequest.setDriverName(user.name);
                        driverInfoRequest.setDriverPhotoUri(user.photo_uri);

                        driversInfoRequestList.add(driverInfoRequest);
                        view.add(driverInfoRequest);
                    }
                } catch (DatabaseException e) {
                    Timber.e(e.getMessage(), e);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private class MyQuotaObserver extends DefaultPresenterObserver<Integer, MyBookingPassengerPresenter> {

        public MyQuotaObserver(MyBookingPassengerPresenter presenter) {
            super(presenter);
        }

        @Override
        public void onNext(Integer value) {
            super.onNext(value);
            view.remove(value);
            bookingPassengerService.refuseDriverRequest();
        }
    }


}
