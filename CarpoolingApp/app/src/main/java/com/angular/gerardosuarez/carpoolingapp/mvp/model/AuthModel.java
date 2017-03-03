package com.angular.gerardosuarez.carpoolingapp.mvp.model;

import com.angular.gerardosuarez.carpoolingapp.service.AuthUser;

/**
 * Created by gerardosuarez on 2/03/17.
 */
public class AuthModel {

    private AuthUser service;

    public AuthModel(AuthUser service) {
        this.service = service;
    }

    public void  authUser(String username, String password){
        service.authUser(username, password);
    }
}
