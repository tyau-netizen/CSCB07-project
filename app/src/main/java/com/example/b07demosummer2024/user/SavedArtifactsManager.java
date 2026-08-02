package com.example.b07demosummer2024.user;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SavedArtifactsManager {
    // Note - artifacts should always be kept sorted by its values
    private final Map<String, String> artifacts;

    public SavedArtifactsManager() {
        this.artifacts = new LinkedHashMap<>();
    }

    public SavedArtifactsManager(Map<String, String> rawMap) {
        if (rawMap != null) {
            this.artifacts = sortByOrderingString(rawMap);
        } else {
            this.artifacts = new LinkedHashMap<>();
        }
    }

    public String add(String artifactId) {
        String firstKey = getFirstOrderKey();
        String newKey = FractionalIndex.generateKeyBetween(null, firstKey);
        artifacts.put(artifactId, newKey);
        sortArtifacts();
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
        sortArtifacts();

        return newKey;
    }

    public Map<String, String> getArtifacts() {
        return new LinkedHashMap<>(artifacts);
    }

    public boolean containsArtifact(String artifactId) {
        return artifacts.containsKey(artifactId);
    }

    private String getFirstOrderKey() {
        if (artifacts.isEmpty()) return null;
        List<String> keys = new ArrayList<>(artifacts.values());
        return keys.get(0);
    }
    private String getLastOrderKey() {
        if (artifacts.isEmpty()) return null;
        List<String> keys = new ArrayList<>(artifacts.values());
        return keys.get(keys.size() - 1);
    }

    private void sortArtifacts() {
        Map<String, String> sortedMap = sortByOrderingString(artifacts);
        artifacts.clear();
        artifacts.putAll(sortedMap);
    }

    private LinkedHashMap<String, String> sortByOrderingString(Map<String, String> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
