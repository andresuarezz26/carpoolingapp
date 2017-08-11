package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import android.text.TextUtils;

import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.BasePresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.DriverInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.User;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyBookingPassengerView;
import com.angular.gerardosuarez.carpoolingapp.service.MyBookingPassengerService;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.ValueEventListener;

import timber.log.Timber;

public class MyBookingPassengerPresenter extends BasePresenter {

    private MyBookingPassengerView view;

    private ValueEventListener bookingPassengerListener;
    private ValueEventListener userListener;

    private MyBookingPassengerService bookingPassengerService;
    private UserService userService;
    private MapPreference mapPreference;

    private String community;
    private String fromOrTo;
    private String date;
    private String hour;

    public MyBookingPassengerPresenter(MyBookingPassengerView view,
                                       MyBookingPassengerService bookingPassengerService,
                                       UserService userService,
                                       MapPreference mapPreference) {
        this.view = view;
        this.bookingPassengerService = bookingPassengerService;
        this.userService = userService;
        this.mapPreference = mapPreference;
    }

    public void init() {
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

    private boolean getMapPreferences() {
        community = mapPreference.getCommunity();
        if (TextUtils.isEmpty(community)) {
            return false;
        }
        fromOrTo = mapPreference.getFromOrTo();
        if (TextUtils.isEmpty(fromOrTo)) {
            return false;
        }
        date = mapPreference.getDate();
        if (TextUtils.isEmpty(date)) {
            return false;
        }
        hour = mapPreference.getTime();
        return !TextUtils.isEmpty(hour);
    }

    //MyPassengerDriverService
    public void getDriversRequestInfo() {
        if (getMapPreferences()) {
            bookingPassengerListener = bookingPassengerService.getPassengerBookings(community, fromOrTo, date, hour)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                try {
                                    view.cleanTexts();
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
        } else {
            view.setInitialSearchingDriverInfo();
        }
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
                        view.setDriverInfo(driverInfoRequest, date, hour);
                        //view.add(driverInfoRequest);
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

}
