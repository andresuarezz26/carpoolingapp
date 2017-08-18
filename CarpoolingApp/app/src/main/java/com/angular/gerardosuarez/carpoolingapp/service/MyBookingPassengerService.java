package com.angular.gerardosuarez.carpoolingapp.service;

import com.angular.gerardosuarez.carpoolingapp.mvp.model.DriverInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.service.base.BaseFirebaseService;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class MyBookingPassengerService extends BaseFirebaseService {

    public DatabaseReference getPassengerBookings(String community, String fromOrTo, String date, String hour) {
        return databaseReference.child(MY_BOOKING_PASSENGER).child(fromOrTo + "-" + community).child(date).child(hour).child("user1");
    }

    public void refuseDriverRequest(String bookingsRoute, DriverInfoRequest driverInfoRequest, String currentUserUid) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(MY_BOOKING_PASSENGER_SLASH + bookingsRoute + currentUserUid + "/" + driverInfoRequest.getKey(), null);
        childUpdates.put(MY_BOOKING_DRIVER_SLASH + bookingsRoute + driverInfoRequest.getKey() + "/" + currentUserUid, null);
        childUpdates.put(bookingsRoute + driverInfoRequest.passengerUid + "/status", PassengerInfoRequest.STATUS_WAITING);
        databaseReference.updateChildren(childUpdates);
    }

    public void cancelMyBooking(String bookingsRoute, String userUid) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(bookingsRoute + userUid, null);
        databaseReference.updateChildren(childUpdates);
    }
}
