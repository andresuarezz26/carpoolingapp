package com.angular.gerardosuarez.carpoolingapp.service;

import android.support.annotation.NonNull;

import com.angular.gerardosuarez.carpoolingapp.mvp.model.DriverInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.service.base.BaseFirebaseService;
import com.angular.gerardosuarez.carpoolingapp.utils.StringUtils;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class DriverMapService extends BaseFirebaseService {

    public DriverMapService() {
        super();
    }

    public DatabaseReference getQuotasPerCommunityOriginDateAndHour(@NonNull String comunity, @NonNull String origin, @NonNull String date, @NonNull String hour) {
        return databaseReference.child(origin + "-" + comunity).child(date).child(hour);
    }

    public void assignBookingToDriverAndPassenger(PassengerInfoRequest passengerInfoRequest,
                                                  DriverInfoRequest driverInfoRequest,
                                                  String bookingsRoute) {
        Map<String, Object> postValuesPassenger = passengerInfoRequest.toMap();
        Map<String, Object> postValuesDriver = driverInfoRequest.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(
                MY_BOOKING_PASSENGER_SLASH + bookingsRoute + passengerInfoRequest.getKey() + StringUtils.SLASH + driverInfoRequest.getKey(),
                postValuesPassenger);
        childUpdates.put(
                MY_BOOKING_DRIVER_SLASH + bookingsRoute + driverInfoRequest.getKey() + StringUtils.SLASH + passengerInfoRequest.getKey(),
                postValuesDriver);
        childUpdates.put(
                bookingsRoute + driverInfoRequest.passengerUid + STATUS,
                PassengerInfoRequest.STATUS_ACCEPTED);
        databaseReference.updateChildren(childUpdates);
    }
}
