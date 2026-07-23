package com.example.b07demosummer2024.user;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class User {
    public static final String USERNAME_KEY = "username";
    public static final String IS_ADMIN_KEY = "isAdmin";
    public static final String SAVED_ARTIFACTS_KEY = "savedArtifacts";

    private final String uid;
    private final String username;
    private final boolean isAdmin;
    private final SavedArtifactsManager savedArtifactsManager;

    // Constructor for creating new user
    public User(String uid, String username) {
        this.uid = uid;
        this.username = username;
        this.isAdmin = false;
        this.savedArtifactsManager = new SavedArtifactsManager();
    }

    // Constructor for fetching full user data from
    public User(DataSnapshot snapshot) {
        this.uid = snapshot.getKey();
        this.username = snapshot.child(USERNAME_KEY).getValue(String.class);
        this.isAdmin = Boolean.TRUE.equals(
                snapshot.child(IS_ADMIN_KEY).getValue(Boolean.class));

        Map<String, String> savedArtifacts = new LinkedHashMap<>();
        for (DataSnapshot child : snapshot.child(SAVED_ARTIFACTS_KEY).getChildren()) {
            savedArtifacts.put(child.getKey(), child.getValue(String.class));
        }

        this.savedArtifactsManager = new SavedArtifactsManager(savedArtifacts);
    }

    public String getUid() {
        return this.uid;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public SavedArtifactsManager getSavedArtifactsManager() {
        return savedArtifactsManager;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put(USERNAME_KEY, this.username);
        map.put(IS_ADMIN_KEY, this.isAdmin);
        map.put(SAVED_ARTIFACTS_KEY, this.savedArtifactsManager.getArtifacts());

        return map;
    }
}
