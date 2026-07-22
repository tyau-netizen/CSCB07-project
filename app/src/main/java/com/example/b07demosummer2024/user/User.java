package com.example.b07demosummer2024.user;

import java.util.LinkedHashMap;
import java.util.Map;

public class User {
    private final String uid;
    private final String username;
    private final SavedArtifactsManager savedArtifacts;

    // Constructor for creating new user
    public User(String uid, String username) {
        this.uid = uid;
        this.username = username;
        this.savedArtifacts = new SavedArtifactsManager();
    }

    // Constructor for fetching full user data
    public User(String uid, String username, Map<String, String> savedArtifacts) {
        this.uid = uid;
        this.username = username;
        this.savedArtifacts = new SavedArtifactsManager(savedArtifacts);
    }

    public String getUid() {
        return this.uid;
    }

    public String getUsername() {
        return this.username;
    }

    public SavedArtifactsManager getSavedArtifactsManager() {
        return savedArtifacts;
    }
}
