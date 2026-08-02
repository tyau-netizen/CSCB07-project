package com.example.b07demosummer2024;

import com.example.b07demosummer2024.model.ArtifactItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Figures out which artifacts are "related" to a given artifact and ranks them.
 * Compares category, material, dynasty/period, and cultural origin against
 * the artifact currently being viewed, and scores everything else based on
 * how many of those match. Whatever the user picks as the main criteria
 * gets weighted higher, the rest just give bonus points.
 */
public class RecommendationEngine {

    /**
     * Null-safe equalsIgnoreCase so we don't crash on artifacts missing
     * fields (a lot of them are, since not every field is required in the DB).
     */
    private boolean safeEqualsIgnoreCase(String s1, String s2) {
        if (s1 == null || s2 == null) return false;
        return s1.equalsIgnoreCase(s2);
    }

    /**
     * Pairs an artifact's lot number with its relevance score so we can
     * sort them together.
     */
    private static class ScoredArtifact implements Comparable<ScoredArtifact> {
        String id;
        int score;

        ScoredArtifact(String id, int score) {
            this.id = id;
            this.score = score;
        }

        @Override
        public int compareTo(ScoredArtifact other) {
            // Sorts in descending order
            return Integer.compare(other.score, this.score);
        }
    }

    /**
     * Ranks every artifact by how related it is to the current one. Base
     * score of 10 if it matches on the chosen criteria, +1 for every other
     * attribute it also happens to match on. Doesn't match the main criteria?
     * Gets skipped entirely.
     *
     * @param current  the artifact currently being viewed
     * @param allItems every artifact in the database, to compare against
     * @param criteria which field to prioritize (Category, Material,
     *                 Dynasty/Period, or Cultural Origin)
     * @return lot numbers of related artifacts, sorted best match first
     */
    public List<String> SortRelatedIds(ArtifactItem current, List<ArtifactItem> allItems, String criteria) {
        List<ScoredArtifact> scoredList = new ArrayList<>();

        String currCat = current.getCategory() != null ? current.getCategory().getDisplayName() : null;
        String currMat = current.getMaterial() != null ? current.getMaterial().getDisplayName() : null;
        String currDyn = current.getDynastyPeriod() != null ? current.getDynastyPeriod().getDisplayName() : null;
        String currCult = current.getCulturalOrigin() != null ? current.getCulturalOrigin() : null;

        for (ArtifactItem item : allItems) {
            // Never recommend the current artifact
            if (safeEqualsIgnoreCase(item.getLotNumber(), current.getLotNumber())) continue;

            // Fetching and turning into strings to handle null exceptions
            String itemCat = item.getCategory() != null ? item.getCategory().getDisplayName() : null;
            String itemMat = item.getMaterial() != null ? item.getMaterial().getDisplayName() : null;
            String itemDyn = item.getDynastyPeriod() != null ? item.getDynastyPeriod().getDisplayName() : null;
            String itemCult = item.getCulturalOrigin() != null ? item.getCulturalOrigin() : null;

            boolean matchCat = safeEqualsIgnoreCase(itemCat, currCat);
            boolean matchMat = safeEqualsIgnoreCase(itemMat, currMat);
            boolean matchDyn = safeEqualsIgnoreCase(itemDyn, currDyn);
            boolean matchCult = safeEqualsIgnoreCase(itemCult, currCult);

            boolean passesCriteria = false;

            if ("Category".equals(criteria) && matchCat) passesCriteria = true;
            else if ("Material".equals(criteria) && matchMat) passesCriteria = true;
            else if ("Dynasty/Period".equals(criteria) && matchDyn) passesCriteria = true;
            else if ("Cultural Origin".equals(criteria) && matchCult) passesCriteria = true;

            int score = 0;

            if (passesCriteria) {
                score = 10; // Base score
                // Add bonus points
                if (!"Category".equals(criteria) && matchCat) score += 1;
                if (!"Material".equals(criteria) && matchMat) score += 1;
                if (!"Dynasty/Period".equals(criteria) && matchDyn) score += 1;
                if (!"Cultural Origin".equals(criteria) && matchCult) score += 1;
            }
            if (score > 0) {
                if (item.getLotNumber() != null) {
                    scoredList.add(new ScoredArtifact(item.getLotNumber(), score));
                }
            }
        }
        Collections.sort(scoredList);

        List<String> result = new ArrayList<>();
        for (ScoredArtifact scored : scoredList) {
            result.add(scored.id);
        }
        return result;
    }
}