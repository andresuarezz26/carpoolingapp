package com.angular.gerardosuarez.carpoolingapp.utils;

/**
 * Created by gerardosuarez
 */
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

    /**
     * Validates if the input is different to null and is not empty
     *
     * @param input String
     * @return
     */
    public boolean stringNotNull(String input) {
        if (input == null || input.equalsIgnoreCase("")) {
            return false;
        }

        return true;
    }
}
