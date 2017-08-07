package com.angular.gerardosuarez.carpoolingapp.mvp.base;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BasePresenter {
    protected DatabaseReference databaseRef;

    protected BasePresenter() {
        this.databaseRef = FirebaseDatabase.getInstance().getReference();
    }
}
