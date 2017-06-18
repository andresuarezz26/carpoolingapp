package com.angular.gerardosuarez.carpoolingapp.service;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverMapService {
    private DatabaseReference databaseRef;

    public DriverMapService() {
        this.databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getQuotasPerCommunityOriginDateAndHour(@NonNull String comunity, @NonNull String origin, @NonNull String date, @NonNull String hour) {
        String rootCommunity = comunity + "" + origin;
        return databaseRef.child(rootCommunity).child(date).child(hour);
    }
}
