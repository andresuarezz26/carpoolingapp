package com.angular.gerardosuarez.carpoolingapp.data.preference.map;

import android.content.Context;

import com.angular.gerardosuarez.carpoolingapp.data.preference.BasePreferenceImpl;

public class MapPreferenceImpl extends BasePreferenceImpl implements MapPreference {

    private final static String CURRENT_ROLE = "role";
    public final static String NAME = "role_preference";
    public static final String DATE = "DATE";
    public static final String TIME = "TIME";
    public static final String COMUNITY = "COMUNITY";
    public static final String FROM_OR_TO = "FROM_OR_TO";
    public static final String ALREADY_REGISTERED = "ALREADY_REGISTERED";

    public MapPreferenceImpl(Context context, String name) {
        super(context, name);
    }

    @Override
    public void putDate(String value) {
        put(DATE, value);
    }

    @Override
    public String getDate() {
        return getString(DATE);
    }

    @Override
    public void putTime(String value) {
        put(TIME, value);
    }

    @Override
    public String getTime() {
        return getString(TIME);
    }

    @Override
    public void putCommunity(String value) {
        put(COMUNITY, value);
    }

    @Override
    public String getCommunity() {
        return getString(COMUNITY);
    }

    @Override
    public void putFromOrTo(String value) {
        put(FROM_OR_TO, value);
    }

    @Override
    public String getFromOrTo() {
        return getString(FROM_OR_TO);
    }

    @Override
    public void putAlreadyRegister(boolean value) {
        put(ALREADY_REGISTERED, value);
    }

    @Override
    public boolean isAlreadyRegister() {
        return getBoolean(ALREADY_REGISTERED);
    }
}
