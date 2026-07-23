package com.example.b07demosummer2024.user;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SavedArtifactsManager {
    private final Map<String, String> artifacts;

    public SavedArtifactsManager() {
        this.artifacts = new LinkedHashMap<>();
    }

    public SavedArtifactsManager(Map<String, String> rawMap) {
        if (rawMap != null) {
            this.artifacts = new LinkedHashMap<>(rawMap);
        } else {
            this.artifacts = new LinkedHashMap<>();
        }
    }

    public String add(String artifactId) {
        String lastKey = getLastOrderKey();
        String newKey = FractionalIndex.generateKeyBetween(lastKey, null);
        artifacts.put(artifactId, newKey);
        return newKey;
    }

    public void remove(String artifactId) {
        artifacts.remove(artifactId);
    }

    public String reorder(String targetId, String prevId, String nextId) {
        String prevKey = (prevId != null) ? artifacts.get(prevId) : null;
        String nextKey = (nextId != null) ? artifacts.get(nextId) : null;

        String newKey = FractionalIndex.generateKeyBetween(prevKey, nextKey);
        artifacts.put(targetId, newKey);
        return newKey;
    }

    public Map<String, String> getArtifacts() {
        return new LinkedHashMap<>(artifacts);
    }

    public boolean containsArtifact(String artifactId) {
        return artifacts.containsKey(artifactId);
    }

    private String getLastOrderKey() {
        if (artifacts.isEmpty()) return null;
        List<String> keys = new ArrayList<>(artifacts.values());
        return keys.get(keys.size() - 1);
    }
}
