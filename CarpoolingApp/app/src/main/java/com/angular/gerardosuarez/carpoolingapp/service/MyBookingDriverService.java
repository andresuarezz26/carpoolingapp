package com.angular.gerardosuarez.carpoolingapp.service;

import android.support.annotation.NonNull;

import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.service.base.BaseFirebaseService;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class MyBookingDriverService extends BaseFirebaseService {


    public DatabaseReference getRequestOfTheDriver(@NonNull String comunity, @NonNull String origin, @NonNull String date, @NonNull String hour, @NonNull String driverId) {
        // FIXME : change the query
        return databaseReference.child(MY_BOOKING_DRIVER).child(origin + "-" + comunity).child(date).child(hour).child(driverId);
    }

    public void cancelPassengerBooking(String bookingsRoute, PassengerInfoRequest passengerInfoRequest) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(MY_BOOKING_PASSENGER_SLASH + bookingsRoute + passengerInfoRequest.getKey() + "/" + passengerInfoRequest.driverUid, null);
        childUpdates.put(MY_BOOKING_DRIVER_SLASH + bookingsRoute + passengerInfoRequest.driverUid + "/" + passengerInfoRequest.getKey(), null);
        childUpdates.put(bookingsRoute + passengerInfoRequest.getKey() + "/status", PassengerInfoRequest.STATUS_WAITING);
        databaseReference.updateChildren(childUpdates);
    }

    public void cancelCurrentRoute() {

    }
}
