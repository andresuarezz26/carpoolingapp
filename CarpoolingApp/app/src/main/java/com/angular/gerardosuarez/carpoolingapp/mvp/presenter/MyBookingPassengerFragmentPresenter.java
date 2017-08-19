package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import android.text.TextUtils;

import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.BaseFragmentPresenter;
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

public class MyBookingPassengerFragmentPresenter extends BaseFragmentPresenter {

    private MyBookingPassengerView view;

    private ValueEventListener bookingPassengerListener;

    private MyBookingPassengerService bookingPassengerService;
    private UserService userService;

    private boolean thereIsDriver = false;
    private boolean thereAreData;
    private DriverInfoRequest currentDriverInfo;


    public MyBookingPassengerFragmentPresenter(MyBookingPassengerView view,
                                               MyBookingPassengerService bookingPassengerService,
                                               UserService userService,
                                               MapPreference mapPreference) {
        super(mapPreference, view);
        this.view = view;
        this.bookingPassengerService = bookingPassengerService;
        this.userService = userService;
        this.mapPreference = mapPreference;
    }

    public void init() {
        if (!getMapPreferencesWithoutErrorMsg()) {
            view.cleanFragmentView();
            thereAreData = false;
        } else {
            thereAreData = true;
            view.init(date, hour);
        }
    }

    public void unsubscribeFirebaseListener() {
        if (bookingPassengerListener != null) {
            databaseRef.removeEventListener(bookingPassengerListener);
        }
    }

    //MyPassengerDriverService
    public void getDriversRequestInfo() {
        if (thereAreData) {
            bookingPassengerListener = bookingPassengerService.getPassengerBookings(community, fromOrTo, date, hour)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                try {
                                    view.cleanFragmentView();
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
                        driverInfoRequest.setKey(uid);
                        view.makeViewsVisible();
                        view.setDriverInfo(driverInfoRequest, date, hour);
                        thereIsDriver = true;
                        currentDriverInfo = driverInfoRequest;
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

    public void onCancelBookingClick() {
        String currentUid = getMyUid();
        if (!TextUtils.isEmpty(currentUid)) {
            if (thereIsDriver) {
                refuseDriverRequest();
            } else {
                cancelMYBooking();
            }
        }
    }

    private void refuseDriverRequest() {
        if (currentDriverInfo != null) {
            if (getMapPreferencesWithoutErrorMsg()) {
                bookingPassengerService.refuseDriverRequest(getRoute(), currentDriverInfo, getMyUid());
                thereIsDriver = false;
                view.setInitialSearchingDriverInfo();
            }
        }
    }

    private void cancelMYBooking() {
        if (getMapPreferencesWithoutErrorMsg()) {
            bookingPassengerService.cancelMyBooking(getRoute(), getMyUid());
            thereIsDriver = false;
            putAlreadyDataChoosen(false);
            view.cleanFragmentView();
            resetMapPreferences();
        }
    }
}
