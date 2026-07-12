package com.example.b07demosummer2024.model;

/**
 * Fixed set of artifact materials.
 */
public enum Material {
    BRONZE("Bronze"),
    STONE("Stone"),
    WOOD("Wood"),
    JADE("Jade"),
    CERAMIC("Ceramic"),
    LACQUERWARE("Lacquerware"),
    IVORY("Ivory"),
    GOLD("Gold"),
    SILVER("Silver"),
    IRON("Iron"),
    MIXED_MEDIA("Mixed Media");

    private final String displayName;

    Material(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
