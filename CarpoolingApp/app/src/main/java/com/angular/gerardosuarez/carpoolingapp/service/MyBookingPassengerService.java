package com.angular.gerardosuarez.carpoolingapp.service;

import com.angular.gerardosuarez.carpoolingapp.mvp.model.DriverInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.service.base.BaseFirebaseService;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gerardosuarez on 7/08/17.
 */

public class MyBookingPassengerService extends BaseFirebaseService {

    private static final String MY_BOOKING_PASSENGER = "peticiones-a-pasajeros";

    public DatabaseReference getPassengerBookings() {
        return databaseReference.child(MY_BOOKING_PASSENGER).child("from-icesi").child("18062017").child("1600").child("passengerMock");
    }

    public void refuseDriverRequest() {
        //String key = databaseReference.child(REQUEST_TO_PASSENGERS).child("from-icesi").child("18062017").child("1600").child(request.userId).push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/peticiones-a-pasajeros/from-icesi/18062017/1600/passengerMock/conductorUid/status/", DriverInfoRequest.STATUS_CANCELED);
        childUpdates.put("/solicitudes-enviadas-conductor/from-icesi/18062017/1600/yo/passengerMock/status", DriverInfoRequest.STATUS_CANCELED);
        //FIXME : change the query
        databaseReference.updateChildren(childUpdates);
        //databaseReference.child(REQUEST_TO_PASSENGERS).child("from-icesi").child("18062017").child("1600").child(passengerUid).child(request.userId).setValue(request);

    }
}
