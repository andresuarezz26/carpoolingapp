package com.angular.gerardosuarez.carpoolingapp.utils;

/**
 * Created by gerardosuarez on 2/03/17.
 */

import com.squareup.otto.Bus;

/**
 * Singleton Class to inicializate BUS_INSTANCE
 */
public final class BusProvider {

    private static final Bus BUS_INSTANCE = new MainPostingBus();

    private BusProvider() {
    }

    public static Bus getInstance() {
        return BUS_INSTANCE;
    }

    public static void register(Object... objects) {
        for (Object o : objects) {
            if (o != null) {
                BUS_INSTANCE.register(o);
            }
        }
    }

    public static void unregister(Object... objects) {
        for (Object o : objects) {
            if (o != null) {
                BUS_INSTANCE.unregister(o);
            }
        }
    }

}
