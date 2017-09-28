package com.angular.gerardosuarez.carpoolingapp.data.preference.role;

public interface RolePreference {
    String ROLE_DRIVER = "driver";

    void putCurrentRole(String value);

    String getCurrentRole();

    void putCurrentTag(String value);

    String getCurrentTag();
}
