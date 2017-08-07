package com.angular.gerardosuarez.carpoolingapp.service;

import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerBooking;
import com.angular.gerardosuarez.carpoolingapp.service.base.BaseFirebaseService;

/**
 * Created by gerardosuarez on 8/07/17.
 */

public class PassengerMapService extends BaseFirebaseService {

    public PassengerMapService() {
        super();
    }

    public void putQuotaPerCommunityOriginDate(PassengerBooking passengerBooking) {
        databaseReference.child("from-icesi").child("18062017").child("1600").child(passengerBooking.userId).setValue(passengerBooking);
    }
}
