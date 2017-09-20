package com.angular.gerardosuarez.carpoolingapp.mvp.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String email;
    public String phone;
    public String photo_uri;
    public String name;
    public String car_plate;
    public String car_color;
    public String car_model;
    public HasBookingAsPassenger hasBookingAsPassenger;
    public HasBookingAsDriver hasBookingAsDriver;
    private String key;

    public User() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}