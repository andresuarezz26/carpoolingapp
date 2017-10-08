package com.angular.gerardosuarez.carpoolingapp.service;

import com.angular.gerardosuarez.carpoolingapp.mvp.model.PassengerBooking;
import com.angular.gerardosuarez.carpoolingapp.service.base.BaseFirebaseService;

public class PassengerMapService extends BaseFirebaseService {

    public PassengerMapService() {
        super();
    }

    public void putPassengerBookingPerCommunityOriginDate(PassengerBooking passengerBooking, String communityNodeName, String date, String hour) {
        databaseReference.child(communityNodeName).child(date).child(hour).child(passengerBooking.getKey()).setValue(passengerBooking);
    }
}
