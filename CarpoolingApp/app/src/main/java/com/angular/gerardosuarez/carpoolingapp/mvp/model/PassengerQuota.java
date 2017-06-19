package com.angular.gerardosuarez.carpoolingapp.mvp.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class PassengerQuota {

    public String description;
    public String userId;
    public double latitude;
    public double longitude;

    public PassengerQuota() {
    }

    public PassengerQuota(String description, String userId) {
        this.description = description;
        this.userId = userId;
    }
}
