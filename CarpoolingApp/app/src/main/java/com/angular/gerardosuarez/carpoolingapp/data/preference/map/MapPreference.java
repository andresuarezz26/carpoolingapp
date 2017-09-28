package com.angular.gerardosuarez.carpoolingapp.data.preference.map;

public interface MapPreference {

    String COMMUNITY_ICESI = "icesi";
    String COMMUNITY_JAVERIANA = "javeriana";
    String FROM = "from";
    String TO = "to";

    void putDate(String value);

    String getDate();

    void putTime(String value);

    String getHour();

    void putCommunity(String value);

    String getCommunity();

    void putFromOrTo(String value);

    String getFromOrTo();

    String getAddress();

    void putAddress(String value);

    void putAlreadyRegister(boolean value);

    boolean isAlreadyRegister();

    void putAlreadyDataChoosen(boolean value);

    boolean isAlreadyDataChoosen();

    boolean isDateSelected();

    void putDateSelected(boolean value);

    boolean isTimeSelected();

    void putTimeSelected(boolean value);

    boolean areTermsAndConditionAccepted();

    void putTermsAndConditionAccepted(boolean value);


}
