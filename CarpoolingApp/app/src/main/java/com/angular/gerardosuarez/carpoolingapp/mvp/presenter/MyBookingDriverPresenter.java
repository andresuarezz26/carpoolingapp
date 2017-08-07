package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import com.angular.gerardosuarez.carpoolingapp.mvp.base.BasePresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.User;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.rx.DefaultPresenterObserver;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyBookingDriverView;
import com.angular.gerardosuarez.carpoolingapp.service.MyBookingDriverService;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MyBookingDriverPresenter extends BasePresenter {

    private MyBookingDriverView view;

    private ValueEventListener bookingDriverListener;
    private ValueEventListener userListener;

    private MyBookingDriverService bookingDriverService;
    private UserService userService;

    private List<PassengerInfoRequest> passengerInfoRequestList;

    public MyBookingDriverPresenter(MyBookingDriverView view,
                                    MyBookingDriverService bookingDriverService,
                                    UserService userService) {
        this.view = view;
        this.bookingDriverService = bookingDriverService;
        this.userService = userService;
    }

    public void init() {
        passengerInfoRequestList = new ArrayList<>();
        view.setAdapterObserver(new MyQuotaObserver(this));
        view.init();
    }

    public void unsubscribeFirebaseListener() {
        if (bookingDriverListener != null) {
            databaseRef.removeEventListener(bookingDriverListener);
        }
        if (userListener != null) {
            databaseRef.removeEventListener(userListener);
        }
    }

    //MyBookingDriverService
    public void getRequestsOfDriver() {
        bookingDriverListener = bookingDriverService.getRequestOfTheDriver("icesi", "from", "18062017", "1600")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        view.removeAll();
                        passengerInfoRequestList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            try {
                                PassengerInfoRequest passengerInfoRequest = snapshot.getValue(PassengerInfoRequest.class);
                                if (passengerInfoRequest != null) {
                                    if (!PassengerInfoRequest.STATUS_CANCELED.equalsIgnoreCase(passengerInfoRequest.status)) {
                                        setPassengerAditionalInfo(snapshot.getKey(), passengerInfoRequest);
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

    private void setPassengerAditionalInfo(final String uid, final PassengerInfoRequest passengerInfoRequest) {
        userService.getUserByUid(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        if (PassengerInfoRequest.STATUS_ACCEPTED.equalsIgnoreCase(passengerInfoRequest.status)) {
                            passengerInfoRequest.setPassengerPhone(user.phone);
                            passengerInfoRequest.setPassengerEmail(user.email);
                        }
                        passengerInfoRequest.setPassengerName(user.name);
                        passengerInfoRequest.setPassengerPhotoUri(user.photo_uri);

                        passengerInfoRequestList.add(passengerInfoRequest);
                        view.add(passengerInfoRequest);
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

    private class MyQuotaObserver extends DefaultPresenterObserver<Integer, MyBookingDriverPresenter> {

        public MyQuotaObserver(MyBookingDriverPresenter presenter) {
            super(presenter);
        }

        @Override
        public void onNext(Integer value) {
            super.onNext(value);
            view.remove(value);
        }
    }


}
