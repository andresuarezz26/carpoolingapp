package com.angular.gerardosuarez.carpoolingapp.utils;

import com.angular.gerardosuarez.carpoolingapp.service.AuthUser;

/**
 * Created by gerardosuarez on 2/03/17.
 */
public final class ServiceUtils {
    private static AuthUser itemService;

    private ServiceUtils() {
    }

    public static final AuthUser getItemService() {
        if (itemService == null) {
            itemService = new AuthUser(BusProvider.getInstance());
        }
        return itemService;
    }
}