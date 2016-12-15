package com.fierydevs.chatapp.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pranit on 07-12-2016.
 */

@IgnoreExtraProperties
public class User {
    private String userId, name, email;

    public User() {

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("name", name);
        result.put("email", email);
        result.put("groups", groups);

        return result;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private Map<String, Boolean> groups;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void setGroups(Map<String, Boolean> groups) {
        this.groups = groups;
    }

    public Map<String, Boolean> getGroups() {
        return groups;
    }
}
