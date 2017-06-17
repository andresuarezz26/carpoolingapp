package com.angular.gerardosuarez.carpoolingapp.utils;

public class Validator {

    public static boolean stringNotNull(String input) {
        return !(input == null || input.equalsIgnoreCase(""));
    }
}
