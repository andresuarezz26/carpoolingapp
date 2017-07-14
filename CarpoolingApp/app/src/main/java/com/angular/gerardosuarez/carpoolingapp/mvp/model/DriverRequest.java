package com.angular.gerardosuarez.carpoolingapp.mvp.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class DriverRequest {
    public String userId;
    public String name;
    public String status;
    public String phoneNumber;

    public DriverRequest() {

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("name", name);
        result.put("status", status);
        result.put("phoneNumber", phoneNumber);
        return result;
    }
}
