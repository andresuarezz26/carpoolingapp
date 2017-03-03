package com.angular.gerardosuarez.carpoolingapp.mvp.event;

/**
 * Created by gerardosuarez on 2/03/17.
 */
public class OnLoginClickedEvent {

    private String username, password;

    public OnLoginClickedEvent(String username, String password) {
        this.username = username;
        this.password= password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
