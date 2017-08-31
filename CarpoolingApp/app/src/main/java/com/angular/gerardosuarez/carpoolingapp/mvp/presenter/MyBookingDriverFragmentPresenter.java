package com.angular.gerardosuarez.carpoolingapp.mvp.presenter;

import android.support.v4.util.Pair;
import android.text.TextUtils;

import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;
import com.angular.gerardosuarez.carpoolingapp.mvp.base.BaseFragmentPresenter;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.User;
import com.angular.gerardosuarez.carpoolingapp.mvp.presenter.rx.DefaultPresenterObserver;
import com.angular.gerardosuarez.carpoolingapp.mvp.view.MyBookingDriverView;
import com.angular.gerardosuarez.carpoolingapp.service.MyBookingDriverService;
import com.angular.gerardosuarez.carpoolingapp.service.UserService;
import com.angular.gerardosuarez.carpoolingapp.utils.StringUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MyBookingDriverFragmentPresenter extends BaseFragmentPresenter {

    private MyBookingDriverView view;

    private ValueEventListener bookingDriverListener;

    private MyBookingDriverService bookingDriverService;
    private UserService userService;

    private List<PassengerInfoRequest> passengerInfoRequestList;

    public MyBookingDriverFragmentPresenter(MyBookingDriverView view,
                                            MyBookingDriverService bookingDriverService,
                                            UserService userService,
                                            MapPreference mapPreference) {
        super(mapPreference, view, userService);
        this.view = view;
        this.bookingDriverService = bookingDriverService;
        this.userService = userService;
        this.mapPreference = mapPreference;
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
    }

    //MyBookingDriverService
    public void getRequestsOfDriver() {
        if (getMapPreferencesWithoutErrorMsg()) {
            if (getMyUid() == null) return;
            bookingDriverListener = bookingDriverService.getRequestOfTheDriver(community, fromOrTo, date, hour, getMyUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            view.removeAll();
                            passengerInfoRequestList.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                try {
                                    PassengerInfoRequest passengerInfoRequest = snapshot.getValue(PassengerInfoRequest.class);
                                    if (passengerInfoRequest != null) {
                                        passengerInfoRequest.setKey(snapshot.getKey());
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

    public void onStartTravel() {
        String myUid = getMyUid();
        if (!TextUtils.isEmpty(myUid)) {
            if (getMapPreferencesWithoutErrorMsg()) {
                bookingDriverService.startRoute(getRoute(), view.getPassengerList(), myUid);
                resetMapPreferences();
                mapPreference.putDateSelected(false);
                mapPreference.putTimeSelected(false);
                mapPreference.putAlreadyDataChoosen(false);
            }
        }
    }

    public void onCancelRoute() {
        String myUid = getMyUid();
        if (!TextUtils.isEmpty(myUid)) {
            if (getMapPreferencesWithoutErrorMsg()) {
                bookingDriverService.cancelCurrentRoute(getRoute(), view.getPassengerList(), myUid);
                resetMapPreferences();
                mapPreference.putDateSelected(false);
                mapPreference.putTimeSelected(false);
                mapPreference.putAlreadyDataChoosen(false);
            }
        }
    }

    private class MyQuotaObserver extends DefaultPresenterObserver<Pair<PassengerInfoRequest, Integer>, MyBookingDriverFragmentPresenter> {

        MyQuotaObserver(MyBookingDriverFragmentPresenter presenter) {
            super(presenter);
        }

        @Override
        public void onNext(Pair<PassengerInfoRequest, Integer> value) {
            super.onNext(value);
            if (getMapPreferencesWithoutErrorMsg()) {
                PassengerInfoRequest passengerInfoRequest = value.first;
                if (passengerInfoRequest != null) {
                    String currentUid = getMyUid();
                    if (currentUid != null) {
                        bookingDriverService.cancelPassengerBooking(StringUtils.buildRoute(community, fromOrTo, date, hour), passengerInfoRequest, currentUid);
                    }
                }
                view.remove(value.second);
            }
        }
    }


}
