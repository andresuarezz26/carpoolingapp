package com.angular.gerardosuarez.carpoolingapp.utils;

public final class Preconditions {
    public static final String MESSAGE_UNRECHEABLE = "Should not have access here";
    public static final String MESSAGE_NOT_NULL = "Object cannot be null";

    private Preconditions() {
    }

    /**
     * Unreachable code.
     */
    public static void unreachable() {
        throw new RuntimeException(MESSAGE_UNRECHEABLE);
    }

    public static void notNull(Object object) {
        if (object == null) {
            throw new RuntimeException(MESSAGE_NOT_NULL);
        }
    }
}
