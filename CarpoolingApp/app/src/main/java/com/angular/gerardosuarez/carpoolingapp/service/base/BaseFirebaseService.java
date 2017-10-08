package com.angular.gerardosuarez.carpoolingapp.service.base;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class BaseFirebaseService {

    protected static final String MY_BOOKING_DRIVER = "solicitudes-enviadas-conductor";
    protected static final String MY_BOOKING_DRIVER_SLASH = "/solicitudes-enviadas-conductor/";
    protected static final String USERS = "users";
    protected static final String AUX_USERS = "users_name_for_client";
    protected static final String MY_BOOKING_PASSENGER = "peticiones-a-pasajeros";
    protected static final String MY_BOOKING_PASSENGER_SLASH = "/peticiones-a-pasajeros/";
    protected static final String STATUS = "/status";

    protected DatabaseReference databaseReference;

    protected BaseFirebaseService() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }
}
