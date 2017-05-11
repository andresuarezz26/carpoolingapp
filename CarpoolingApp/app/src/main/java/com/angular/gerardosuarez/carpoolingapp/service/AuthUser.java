package com.angular.gerardosuarez.carpoolingapp.service;

import com.angular.gerardosuarez.carpoolingapp.mvp.event.OnLoginEvent;
import com.squareup.otto.Bus;

/**
 * Created by gerardosuarez on 2/03/17.
 */
public class AuthUser {

    private Bus bus;

    public AuthUser(Bus bus) {
        this.bus = bus;
    }

    public void authUser(String username, String password) {
        //Do query to firebase here
        boolean resultLogin = true;

        bus.post(new OnLoginEvent(resultLogin));
    }
}
