package com.angular.gerardosuarez.carpoolingapp.service.base;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class BaseFirebaseService {

    protected DatabaseReference databaseReference;

    protected BaseFirebaseService() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }

}
