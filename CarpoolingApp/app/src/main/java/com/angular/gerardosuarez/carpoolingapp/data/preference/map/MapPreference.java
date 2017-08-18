package com.angular.gerardosuarez.carpoolingapp.data.preference.map;

public interface MapPreference {

    public final static String COMMUNITY_ICESI = "icesi";
    public final static String COMMUNITY_JAVERIANA = "javeriana";
    public final static String FROM = "from";
    public final static String TO = "to";

    void putDate(String value);

    String getDate();

    void putTime(String value);

    String getTime();

    void putCommunity(String value);

    String getCommunity();

    void putFromOrTo(String value);

    String getFromOrTo();

    void putAlreadyRegister(boolean value);

    boolean isAlreadyRegister();
}
