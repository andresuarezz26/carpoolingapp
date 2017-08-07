package com.angular.gerardosuarez.carpoolingapp.mvp.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class PassengerBooking {
    public String description;
    public String userId;
    public double latitude;
    public double longitude;

    public PassengerBooking() {
    }

    public PassengerBooking(String description, String userId) {
        this.description = description;
        this.userId = userId;
    }


}
