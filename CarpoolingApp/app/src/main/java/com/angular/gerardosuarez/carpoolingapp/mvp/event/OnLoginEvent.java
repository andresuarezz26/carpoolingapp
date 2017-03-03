package com.angular.gerardosuarez.carpoolingapp.mvp.event;

/**
 * Created by gerardosuarez on 2/03/17.
 */
public class OnLoginEvent {

    private boolean success;

    public OnLoginEvent(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
