package com.angular.gerardosuarez.carpoolingapp.mvp.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class PassengerQuota {

    private String description;
    private String userId;

    public PassengerQuota() {
    }

    public PassengerQuota(String description, String userId) {
        this.description = description;
        this.userId = userId;
    }
}
