package com.angular.gerardosuarez.carpoolingapp.service;

import com.angular.gerardosuarez.carpoolingapp.mvp.model.User;
import com.angular.gerardosuarez.carpoolingapp.service.base.BaseFirebaseService;
import com.google.firebase.database.DatabaseReference;

public class UserService extends BaseFirebaseService {

    public UserService() {
        super();
    }

    public void updateUser(User user) {

    }

    public DatabaseReference getUserByUid(String uid) {
        return databaseReference.child("users").child(uid);
    }

    public void deleteUser(String key) {

    }
}