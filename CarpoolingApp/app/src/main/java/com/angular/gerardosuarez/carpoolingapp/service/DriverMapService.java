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
        return databaseReference.child("from-icesi").child("18062017").child("1600");
    }

    public void putInfoRequestToPassengerAndDriver(PassengerInfoRequest passengerInfoRequest, DriverInfoRequest driverInfoRequest) {
        //String key = databaseReference.child(REQUEST_TO_PASSENGERS).child("from-icesi").child("18062017").child("1600").child(request.userId).push().getKey();
        Map<String, Object> postValuesPassenger = passengerInfoRequest.toMap();
        Map<String, Object> postValuesDriver = driverInfoRequest.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/peticiones-a-pasajeros/from-icesi/18062017/1600/" + passengerInfoRequest.getKey() + "/" + driverInfoRequest.getKey(), postValuesPassenger);
        childUpdates.put("/solicitudes-enviadas-conductor/from-icesi/18062017/1600/" + driverInfoRequest.getKey() + "/" + passengerInfoRequest, postValuesDriver);
        //FIXME : change the query
        databaseReference.updateChildren(childUpdates);
        //databaseReference.child(REQUEST_TO_PASSENGERS).child("from-icesi").child("18062017").child("1600").child(passengerUid).child(request.userId).setValue(request);
    }
}
