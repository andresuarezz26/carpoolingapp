package com.angular.gerardosuarez.carpoolingapp.mvp.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class PassengerInfoRequest {

    public static final String STATUS_WAITING = "waiting";
    public static final String STATUS_CANCELED = "canceled";
    public static final String STATUS_ACCEPTED = "accepted";

    private String passengerName;
    private String passengerPhone;
    private String passengerEmail;
    private String passengerPhotoUri;
    private String key;

    public String status;
    public String address;
    public String driverUid;

    public PassengerInfoRequest() {

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("status", status);
        result.put("address", address);
        result.put("driverUid", driverUid);
        return result;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerPhone() {
        return passengerPhone;
    }

    public void setPassengerPhone(String passengerPhone) {
        this.passengerPhone = passengerPhone;
    }

    public String getPassengerEmail() {
        return passengerEmail;
    }

    public void setPassengerEmail(String passengerEmail) {
        this.passengerEmail = passengerEmail;
    }

    public String getPassengerPhotoUri() {
        return passengerPhotoUri;
    }

    public void setPassengerPhotoUri(String passengerPhotoUri) {
        this.passengerPhotoUri = passengerPhotoUri;
    }
}
