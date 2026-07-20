package com.example.b07demosummer2024.user;

import java.util.LinkedHashMap;
import java.util.Map;

public class User {
    private final String uid;
    private final String username;
    private final Map<String, String> savedArtifacts;

    // Constructor for creating new user
    public User(String uid, String username) {
        this.uid = uid;
        this.username = username;
        this.savedArtifacts = new LinkedHashMap<>();
    }

    // Constructor for fetching full user data
    public User(String uid, String username, Map<String, String> savedArtifacts) {
        this.uid = uid;
        this.username = username;
        this.savedArtifacts = savedArtifacts;
    }

    public String getUid() {
        return this.uid;
    }

    public String getUsername() {
        return this.username;
    }

    public Map<String, String> getSavedArtifacts() {
        return this.savedArtifacts;
    }


}
