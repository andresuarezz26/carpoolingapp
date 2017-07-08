package com.angular.gerardosuarez.carpoolingapp.service;

import android.support.annotation.NonNull;

import com.angular.gerardosuarez.carpoolingapp.service.base.BaseFirebaseService;
import com.google.firebase.database.DatabaseReference;

public class DriverMapService extends BaseFirebaseService{

    public DriverMapService() {
        super();
    }

    public DatabaseReference getQuotasPerCommunityOriginDateAndHour(@NonNull String comunity, @NonNull String origin, @NonNull String date, @NonNull String hour) {
        String rootCommunity = origin + "-" + comunity;
        String route = rootCommunity + date + hour + "diegobarbosa180620171600";
        return databaseReference.child("from-icesi").child("18062017").child("1600");
    }
}
