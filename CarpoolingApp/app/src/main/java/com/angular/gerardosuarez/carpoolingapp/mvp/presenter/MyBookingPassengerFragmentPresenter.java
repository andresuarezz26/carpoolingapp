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

    //Constructor
    private MyBookingPassengerView view;
    private MyBookingPassengerService bookingPassengerService;
    private UserService userService;

    //listeners
    private ValueEventListener bookingPassengerListener;

    private boolean thereIsDriver = false;
    private boolean thereAreData;
    private DriverInfoRequest currentDriverInfo;


    public MyBookingPassengerFragmentPresenter(MyBookingPassengerView view,
                                               MyBookingPassengerService bookingPassengerService,
                                               UserService userService,
                                               MapPreference mapPreference) {
        super(mapPreference, view, userService);
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

    //region MyDriverRequest
    public void getDriversRequestInfo() {
        if (thereAreData) {
            if (getMyUid() == null) return;
            bookingPassengerListener = bookingPassengerService.getPassengerBookings(community, fromOrTo, date, hour, getMyUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            processDriverInfoRequest(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Timber.e(databaseError.toString(), databaseError);
                        }
                    });
        }

    }

    private void processDriverInfoRequest(DataSnapshot dataSnapshot) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            try {
                view.cleanFragmentView();
                DriverInfoRequest driverInfoRequest = snapshot.getValue(DriverInfoRequest.class);
                if (driverInfoRequest != null) {
                    if (!DriverInfoRequest.STATUS_CANCELED.equalsIgnoreCase(driverInfoRequest.status)) {
                        setDriverAditionalInfoQuery(snapshot.getKey(), driverInfoRequest);
                    }
                }
            } catch (DatabaseException e) {
                Timber.e(e.getMessage(), e);
            }
        }
    }

    private void setDriverAditionalInfoQuery(final String uid, final DriverInfoRequest driverInfoRequest) {
        userService.getUserByUid(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                processDriverAdditionalInfo(dataSnapshot, driverInfoRequest, uid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void processDriverAdditionalInfo(DataSnapshot dataSnapshot, final DriverInfoRequest driverInfoRequest, final String uid) {
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
                view.makeButtonAndImageVisibles();
                view.setDriverInfo(driverInfoRequest, date, hour);
                currentDriverInfo = driverInfoRequest;
                thereIsDriver = true;
            }
        } catch (DatabaseException e) {
            Timber.e(e.getMessage(), e);
        }
    }

    //endregion

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
                if (!TextUtils.isEmpty(getMyUid())) {
                    bookingPassengerService.refuseDriverRequest(getRoute(), currentDriverInfo, getMyUid());
                }
                thereIsDriver = false;
                view.setInitialSearchingDriverInfo();
            }
        }
    }

    private void cancelMYBooking() {
        if (getMapPreferencesWithoutErrorMsg()) {
            if (!TextUtils.isEmpty(getMyUid())) {
                bookingPassengerService.cancelMyBooking(getRoute(), getMyUid());
                thereIsDriver = false;
                putAlreadyDataChoosen(false);
                view.cleanFragmentView();
                resetMapPreferencesUsedInMapFragment();
            }
        }
    }

    public void unsuscribeFirebaseListener() {
        if (bookingPassengerListener != null) {
            databaseRef.removeEventListener(bookingPassengerListener);
        }
    }
}
