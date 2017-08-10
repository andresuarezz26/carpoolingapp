package com.angular.gerardosuarez.carpoolingapp.service;

import android.support.annotation.NonNull;

import com.angular.gerardosuarez.carpoolingapp.mvp.model.DriverInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerInfoRequest;
import com.angular.gerardosuarez.carpoolingapp.service.base.BaseFirebaseService;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class DriverMapService extends BaseFirebaseService {

    private static final String REQUEST_TO_PASSENGERS = "peticiones-a-pasajeros";

    public DriverMapService() {
        super();
    }

    public DatabaseReference getQuotasPerCommunityOriginDateAndHour(@NonNull String comunity, @NonNull String origin, @NonNull String date, @NonNull String hour) {
        // FIXME : change the query
        return databaseReference.child(origin + "-" + comunity).child(date).child(hour);
    }

    public void assignBookingToDriverAndPassenger(PassengerInfoRequest passengerInfoRequest,
                                                  DriverInfoRequest driverInfoRequest,
                                                  String route) {
        //String key = databaseReference.child(REQUEST_TO_PASSENGERS).child("from-icesi").child("18062017").child("1600").child(request.userId).push().getKey();
        Map<String, Object> postValuesPassenger = passengerInfoRequest.toMap();
        Map<String, Object> postValuesDriver = driverInfoRequest.toMap();
        Map<String, Object> postValuesPassengerBooking = new HashMap<>();
        postValuesPassengerBooking.put("status", PassengerInfoRequest.STATUS_ACCEPTED);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/peticiones-a-pasajeros/" + route + passengerInfoRequest.getKey() + "/" + driverInfoRequest.getKey(), postValuesPassenger);
        childUpdates.put("/solicitudes-enviadas-conductor/" + route + driverInfoRequest.getKey() + "/" + passengerInfoRequest.getKey(), postValuesDriver);
        childUpdates.put(route + driverInfoRequest.getKey() + "/status", PassengerInfoRequest.STATUS_ACCEPTED);
        //FIXME : change the query
        databaseReference.updateChildren(childUpdates);
        //databaseReference.child(REQUEST_TO_PASSENGERS).child("from-icesi").child("18062017").child("1600").child(passengerUid).child(request.userId).setValue(request);
    }
}
