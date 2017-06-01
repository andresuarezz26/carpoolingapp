package com.angular.gerardosuarez.carpoolingapp.service;

import com.angular.gerardosuarez.carpoolingapp.service.rx.DefaultServicePublisher;

public class AuthUserService extends DefaultServicePublisher<Boolean> {

    public void authUser(String username, String password) {
        //Do query to firebase here
        Boolean resultLogin = true;

        notifyAuthResult(resultLogin);
    }
}
