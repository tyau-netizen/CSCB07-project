package com.example.b07demosummer2024.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Locale;

/**
 * Data model representing a single museum artifact.

 * Mandatory fields (per requirements): lotNumber, name, description, category, material,
                                        dynastyPeriod.
 * Everything else is optional and may be null/empty.
 */
public class ArtifactItem {

    // ---- Mandatory fields ----
    private String lotNumber;          // unique identifier
    private String name;
    private String description;
    private Category category;
    private Material material;
    private DynastyPeriod dynastyPeriod;

    // ---- Optional fields ----
    private String culturalOrigin;
    private String dimensions;
    private String conditionReport;
    private String currentLocation;
    private String acquisitionMethod;
    private String provenance;
    private String accessionNumber;
    private String notes;
    private String imageUri;

    // Whether the current user has saved/bookmarked this artifact.
    private boolean saved;

    public ArtifactItem() {
        // Empty constructor
    }

    public ArtifactItem(String lotNumber, String name, String description, Category category,
                    Material material, DynastyPeriod dynastyPeriod) {
        this.lotNumber = lotNumber;
        this.name = name;
        this.description = description;
        this.category = category;
        this.material = material;
        this.dynastyPeriod = dynastyPeriod;
    }

    // ---- Getters and setters ----

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public DynastyPeriod getDynastyPeriod() {
        return dynastyPeriod;
    }

    public void setDynastyPeriod(DynastyPeriod dynastyPeriod) {
        this.dynastyPeriod = dynastyPeriod;
    }

    public String getCulturalOrigin() {
        return culturalOrigin;
    }

    public void setCulturalOrigin(String culturalOrigin) {
        this.culturalOrigin = culturalOrigin;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getConditionReport() {
        return conditionReport;
    }

    public void setConditionReport(String conditionReport) {
        this.conditionReport = conditionReport;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getAcquisitionMethod() {
        return acquisitionMethod;
    }

    public void setAcquisitionMethod(String acquisitionMethod) {
        this.acquisitionMethod = acquisitionMethod;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    /**
     * Keyword search helper, e.g.:

     *   List<Artifact> results = new ArrayList<>();
     *   for (Artifact a : allArtifacts) {
     *       if (a.matchesKeyword(query)) results.add(a);
     *   }
     */
    public boolean matchesKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return true; // empty search
        }
        String q = keyword.toLowerCase(Locale.getDefault()).trim();

        return containsIgnoreCase(lotNumber, q)
                || containsIgnoreCase(name, q)
                || containsIgnoreCase(description, q)
                || (category != null && containsIgnoreCase(category.getDisplayName(), q))
                || (material != null && containsIgnoreCase(material.getDisplayName(), q))
                || (dynastyPeriod != null && containsIgnoreCase(dynastyPeriod.getDisplayName(), q))
                || containsIgnoreCase(culturalOrigin, q)
                || containsIgnoreCase(dimensions, q)
                || containsIgnoreCase(conditionReport, q)
                || containsIgnoreCase(currentLocation, q)
                || containsIgnoreCase(acquisitionMethod, q)
                || containsIgnoreCase(provenance, q)
                || containsIgnoreCase(accessionNumber, q)
                || containsIgnoreCase(notes, q);
    }

    private boolean containsIgnoreCase(String field, String query) {
        return field != null && field.toLowerCase(Locale.getDefault()).contains(query);
    }
}
