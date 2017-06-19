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
        String rootCommunity = origin + "-" + comunity;
        String route = rootCommunity + date + hour + "diegobarbosa180620171600";
        return databaseRef.child("from-icesi").child("18062017").child("1600").child("diegobarbosa180620171600");
    }
}
