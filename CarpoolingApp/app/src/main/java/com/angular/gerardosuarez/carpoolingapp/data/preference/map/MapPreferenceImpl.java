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
    public static final String ALREADY_DATA_CHOOSEN = "ALREADY_DATA_CHOOSEN";
    public static final String IS_DATE_SELECTED = "IS_DATE_SELECTED";
    public static final String IS_TIME_SELECTED = "IS_TIME_SELECTED";

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

    @Override
    public void putAlreadyDataChoosen(boolean value) {
        put(ALREADY_DATA_CHOOSEN, value);
    }

    @Override
    public boolean isAlreadyDataChoosen() {
        return getBoolean(ALREADY_DATA_CHOOSEN);
    }

    @Override
    public boolean isDateSelected() {
        return getBoolean(IS_DATE_SELECTED);
    }

    @Override
    public void putDateSelected(boolean value) {
        put(IS_DATE_SELECTED, value);
    }

    @Override
    public boolean isTimeSelected() {
        return getBoolean(IS_TIME_SELECTED);
    }

    @Override
    public void putTimeSelected(boolean value) {
        put(IS_TIME_SELECTED, value);
    }
}
