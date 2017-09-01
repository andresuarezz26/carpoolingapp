package com.angular.gerardosuarez.carpoolingapp.data.preference.role;

public interface RolePreference {

    void putCurrentRole(String value);

    String getCurrentRole();

    void putCurrentTag(String value);

    String getCurrentTag();
}
