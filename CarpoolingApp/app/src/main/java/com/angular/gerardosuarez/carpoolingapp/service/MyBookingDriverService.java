package com.angular.gerardosuarez.carpoolingapp.service;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.service.base.BaseFirebaseService;
import com.angular.gerardosuarez.carpoolingapp.utils.StringUtils;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBookingDriverService extends BaseFirebaseService {


    public DatabaseReference getRequestOfTheDriver(@NonNull String comunity, @NonNull String origin, @NonNull String date, @NonNull String hour, @NonNull String driverId) {
        // FIXME : change the query
        return databaseReference.child(MY_BOOKING_DRIVER).child(origin + "-" + comunity).child(date).child(hour).child(driverId);
    }

    public void cancelPassengerBooking(@NonNull String bookingsRoute,
                                       @NonNull PassengerInfoRequest passengerInfoRequest,
                                       @NonNull String currentUid) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(MY_BOOKING_PASSENGER_SLASH + bookingsRoute + passengerInfoRequest.getKey() + "/" + currentUid, null);
        childUpdates.put(MY_BOOKING_DRIVER_SLASH + bookingsRoute + currentUid + "/" + passengerInfoRequest.getKey(), null);
        childUpdates.put(bookingsRoute + passengerInfoRequest.getKey() + "/status", PassengerInfoRequest.STATUS_WAITING);
        databaseReference.updateChildren(childUpdates);
    }

    public void cancelCurrentRoute(@NonNull String bookingsRoute,
                                   @NonNull List<PassengerInfoRequest> passengerUidsList,
                                   @NonNull String currentUid) {
        for (PassengerInfoRequest passenger : passengerUidsList) {
            String passengerUid = passenger.getKey();
            if (!TextUtils.isEmpty(passengerUid)) {
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(MY_BOOKING_PASSENGER_SLASH + bookingsRoute + passengerUid + StringUtils.SLASH + currentUid, null);
                childUpdates.put(MY_BOOKING_DRIVER_SLASH + bookingsRoute + currentUid + StringUtils.SLASH + passengerUid, null);
                childUpdates.put(bookingsRoute + currentUid + STATUS, PassengerInfoRequest.STATUS_WAITING);
                databaseReference.updateChildren(childUpdates);
            }
        }
    }

    public void startRoute(@NonNull String bookingsRoute,
                           @NonNull List<PassengerInfoRequest> passengerUidsList,
                           @NonNull String currentUid) {
        for (PassengerInfoRequest passenger : passengerUidsList) {
            String passengerUid = passenger.getKey();
            if (!TextUtils.isEmpty(passengerUid)) {
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(MY_BOOKING_PASSENGER_SLASH + bookingsRoute + passengerUid + StringUtils.SLASH + currentUid, null);
                childUpdates.put(MY_BOOKING_DRIVER_SLASH + bookingsRoute + currentUid + StringUtils.SLASH + passengerUid, null);
                childUpdates.put(bookingsRoute + currentUid + STATUS, PassengerInfoRequest.STATUS_ACCEPTED);
                databaseReference.updateChildren(childUpdates);
            }
        }
    }
}
