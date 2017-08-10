package com.angular.gerardosuarez.carpoolingapp.service;

import android.support.annotation.NonNull;

import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.service.base.BaseFirebaseService;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class MyBookingDriverService extends BaseFirebaseService {

    private static final String MY_BOOKING_DRIVER = "solicitudes-enviadas-conductor";

    public DatabaseReference getRequestOfTheDriver(@NonNull String comunity, @NonNull String origin, @NonNull String date, @NonNull String hour, @NonNull String driverId) {
        // FIXME : change the query
        return databaseReference.child(MY_BOOKING_DRIVER).child(origin+"-"+comunity).child(date).child(hour).child(driverId);
    }

    public void cancelRequest() {
        //String key = databaseReference.child(REQUEST_TO_PASSENGERS).child("from-icesi").child("18062017").child("1600").child(request.userId).push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/peticiones-a-pasajeros/from-icesi/18062017/1600/passengerMock/conductorUid/status/", PassengerInfoRequest.STATUS_CANCELED);
        childUpdates.put("/solicitudes-enviadas-conductor/from-icesi/18062017/1600/yo/passengerMock/status", PassengerInfoRequest.STATUS_CANCELED);
        //FIXME : change the query
        databaseReference.updateChildren(childUpdates);
        //databaseReference.child(REQUEST_TO_PASSENGERS).child("from-icesi").child("18062017").child("1600").child(passengerUid).child(request.userId).setValue(request);
    }

    public void cancelCurrentRoute() {

    }
}
