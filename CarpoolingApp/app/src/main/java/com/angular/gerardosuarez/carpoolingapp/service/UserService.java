package com.angular.gerardosuarez.carpoolingapp.service;

import android.support.annotation.NonNull;

import com.angular.gerardosuarez.carpoolingapp.mvp.model.User;
import com.angular.gerardosuarez.carpoolingapp.service.base.BaseFirebaseService;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class UserService extends BaseFirebaseService {

    public UserService() {
        super();
    }

    public void createOrUpdateUser(User user) {
        databaseReference.child(USERS).child(user.getKey()).setValue(user);
    }

    public void createAuxUserNode(String nameAndUid) {
        databaseReference.child(AUX_USERS).setValue(nameAndUid);
    }

    public DatabaseReference getUserByUid(String uid) {
        return databaseReference.child("users").child(uid);
    }

    public User mapFirebaseUserToUser(@NonNull FirebaseUser user) {
        User appUser = new User();
        appUser.setKey(user.getUid());
        appUser.name = user.getDisplayName();
        if (user.getPhotoUrl() != null) {
            appUser.photo_uri = user.getPhotoUrl().toString();
        }
        appUser.email = user.getEmail();
        return appUser;
    }
}