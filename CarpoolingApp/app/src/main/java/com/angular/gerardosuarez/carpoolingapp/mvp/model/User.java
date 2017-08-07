package com.angular.gerardosuarez.carpoolingapp.mvp.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String email;
    public String phone;
    public String photo_uri;
    public String name;

    public User() {

    }

}