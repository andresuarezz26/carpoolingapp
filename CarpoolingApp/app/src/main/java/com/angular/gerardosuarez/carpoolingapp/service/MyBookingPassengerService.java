package com.angular.gerardosuarez.carpoolingapp.service;

import android.support.annotation.NonNull;

import com.angular.gerardosuarez.carpoolingapp.mvp.model.DriverInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.service.base.BaseFirebaseService;
import com.angular.gerardosuarez.carpoolingapp.utils.StringUtils;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class MyBookingPassengerService extends BaseFirebaseService {

    public DatabaseReference getPassengerBookings(String community, String fromOrTo, String date, String hour, String myUid) {
        return databaseReference.child(MY_BOOKING_PASSENGER).child(fromOrTo + "-" + community).child(date).child(hour).child(myUid);
    }

    public void refuseDriverRequest(@NonNull String bookingsRoute,
                                    @NonNull DriverInfoRequest driverInfoRequest,
                                    @NonNull String currentUserUid) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(MY_BOOKING_PASSENGER_SLASH + bookingsRoute + currentUserUid + StringUtils.SLASH + driverInfoRequest.getKey(), null);
        childUpdates.put(MY_BOOKING_DRIVER_SLASH + bookingsRoute + driverInfoRequest.getKey() + StringUtils.SLASH + currentUserUid, null);
        childUpdates.put(bookingsRoute + currentUserUid + STATUS, PassengerInfoRequest.STATUS_WAITING);
        databaseReference.updateChildren(childUpdates);
    }

    public void cancelMyBooking(@NonNull String bookingsRoute, @NonNull String userUid) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(bookingsRoute + userUid, null);
        databaseReference.updateChildren(childUpdates);
    }
}
