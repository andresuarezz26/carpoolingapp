package com.angular.gerardosuarez.carpoolingapp.mvp.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class DriverInfoRequest {
    public static final String STATUS_WAITING = "waiting";
    public static final String STATUS_CANCELED = "canceled";
    public static final String STATUS_ACCEPTED = "accepted";

    private String driverName;
    private String driverPhone;
    private String driverEmail;
    private String driverPhotoUri;
    private String key;

    public String status;
    public String address;
    public String passengerUid;

    public DriverInfoRequest(){

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("status", status);
        result.put("address", address);
        result.put("passengerUid", passengerUid);
        return result;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getDriverPhotoUri() {
        return driverPhotoUri;
    }

    public void setDriverPhotoUri(String driverPhotoUri) {
        this.driverPhotoUri = driverPhotoUri;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
