package com.example.b07demosummer2024.model;

/**
 * Fixed set of artifact categories.
 **/
public enum Category {
    PAINTING_AND_CALLIGRAPHY("Painting and Calligraphy"),
    CERAMICS("Ceramics"),
    BRONZE_WARE("Bronze Ware"),
    LACQUERWARE("Lacquerware"),
    JADE_WARE("Jade Ware"),
    ENAMEL_WARE("Enamel Ware"),
    GLASSWARE("Glassware"),
    FURNITURE("Furniture"),
    EMBROIDERY_AND_TEXTILES("Embroidery and Textiles"),
    DOCUMENTS_AND_ARCHIVES("Documents and Archives"),
    GOLD_AND_SILVERWARE("Gold and Silverware"),
    CLOCKS_AND_WATCHES("Clocks and Watches"),
    RELIGIOUS_ARTIFACTS("Religious Artifacts"),
    DAILY_USE_ITEMS("Daily-use Items"),
    WEAPONRY("Weaponry"),
    MISCELLANEOUS_ARTIFACTS("Miscellaneous Artifacts");

    private final String displayName;

    Category(String displayName) {
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
