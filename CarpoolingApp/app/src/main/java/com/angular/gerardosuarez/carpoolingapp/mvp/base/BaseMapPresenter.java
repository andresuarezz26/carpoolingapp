package com.angular.gerardosuarez.carpoolingapp.mvp.base;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BaseMapPresenter {
    protected DatabaseReference databaseRef;

    protected BaseMapPresenter() {
        this.databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    public void unsubscribe() {
    }

    public void subscribe() {
    }
}
