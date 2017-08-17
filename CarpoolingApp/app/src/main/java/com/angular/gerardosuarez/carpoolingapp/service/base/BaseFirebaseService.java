package com.angular.gerardosuarez.carpoolingapp.service.base;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class BaseFirebaseService {

    protected static final String MY_BOOKING_DRIVER = "solicitudes-enviadas-conductor";
    protected static final String MY_BOOKING_DRIVER_SLASH = "/solicitudes-enviadas-conductor/";
    protected static final String MY_BOOKING_PASSENGER = "peticiones-a-pasajeros";
    protected static final String MY_BOOKING_PASSENGER_SLASH = "/peticiones-a-pasajeros/";

    protected DatabaseReference databaseReference;

    protected BaseFirebaseService() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }

}
