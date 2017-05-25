package com.angular.gerardosuarez.carpoolingapp.utils;

public class Validator {

    public static Validator validator;

    public Validator() {

    }

    public static Validator getInstance() {
        if (validator == null) {
            validator = new Validator();
        }

        return validator;
    }

    public boolean stringNotNull(String input) {
        if (input == null || input.equalsIgnoreCase("")) {
            return false;
        }

        return true;
    }
}
