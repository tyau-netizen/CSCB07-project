package com.example.b07demosummer2024;

import android.os.Build;

import com.example.b07demosummer2024.model.ArtifactItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class RecommendationEngine {

    // Helper method to safely compare strings, even if either of them is null
    private boolean safeEqualsIgnoreCase(String s1, String s2) {
        if (s1 == null || s2 == null) return false;
        return s1.equalsIgnoreCase(s2);
    }

    // Helper method to safely check if a description contains a name
    private boolean safeContainsIgnoreCase(String fullText, String subText) {
        if (fullText == null || subText == null) return false;
        // The API version wrapper is gone, fixing your "missing return statement" error!
        return fullText.toLowerCase(Locale.ROOT).contains(subText.toLowerCase(Locale.ROOT));
    }

    private static class ScoredArtifact implements Comparable<ScoredArtifact> {
        String id;
        int score;

        ScoredArtifact(String id, int score) {
            this.id = id;
            this.score = score;
        }

        @Override
        public int compareTo(ScoredArtifact other) {
            // Sort descending (highest score first)
            return Integer.compare(other.score, this.score);
        }
    }

    public List<String> SortRelatedIds(ArtifactItem current, List<ArtifactItem> allItems, String criteria) {
        List<ScoredArtifact> scoredList = new ArrayList<>();

        // 1. Grab the Plaintext strings for the CURRENT artifact (safely handling nulls)
        String currCat = current.getCategory() != null ? current.getCategory().getDisplayName() : null;
        String currMat = current.getMaterial() != null ? current.getMaterial().getDisplayName() : null;
        String currDyn = current.getDynastyPeriod() != null ? current.getDynastyPeriod().getDisplayName() : null;

        for (ArtifactItem item : allItems) {
            // Never recommend the current artifact
            if (safeEqualsIgnoreCase(item.getLotNumber(), current.getLotNumber())) continue;

            int score = 0;

            // Turn into Plaintext strings for these fields to handle null...
            String itemCat = item.getCategory() != null ? item.getCategory().getDisplayName() : null;
            String itemMat = item.getMaterial() != null ? item.getMaterial().getDisplayName() : null;
            String itemDyn = item.getDynastyPeriod() != null ? item.getDynastyPeriod().getDisplayName() : null;

            // Scoring Algorithm
            // Now we just compare the plain strings we extracted above!
            if ("Category".equals(criteria) && safeEqualsIgnoreCase(itemCat, currCat)) score += 10;
            if ("Material".equals(criteria) && safeEqualsIgnoreCase(itemMat, currMat)) score += 10;
            if ("Dynasty/Period".equals(criteria) && safeEqualsIgnoreCase(itemDyn, currDyn)) score += 10;

            // Basic similarity (points for matching details)
            if (safeEqualsIgnoreCase(itemCat, currCat)) score += 3;
            if (safeEqualsIgnoreCase(itemMat, currMat)) score += 2;
            if (safeEqualsIgnoreCase(itemDyn, currDyn)) score += 1;

            // Safe check for the description containing the name
            if (safeContainsIgnoreCase(item.getDescription(), current.getName())) score += 1;

            if (score > 0) {
                // Ensure we don't add null IDs to our sorted list
                if (item.getLotNumber() != null) {
                    scoredList.add(new ScoredArtifact(item.getLotNumber(), score));
                }
            }
        }

        // Sort by score
        Collections.sort(scoredList);

        // Convert to list of IDs (no cap, return all matches)
        List<String> sortedIds = new ArrayList<>();
        for (ScoredArtifact sa : scoredList) {
            sortedIds.add(sa.id);
        }
        return sortedIds;
    }
}