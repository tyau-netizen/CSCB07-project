package com.example.b07demosummer2024.model;

/**
 * Fixed set of dynasty/period options.
 */
public enum DynastyPeriod {
    SHANG("Shang Dynasty (c. 1600-1046 BCE)"),
    WESTERN_ZHOU("Western Zhou Dynasty (c. 1046-771 BCE)"),
    EASTERN_ZHOU("Eastern Zhou Dynasty (770-256 BCE)"),
    QIN("Qin Dynasty (221-206 BCE)"),
    HAN("Han Dynasty (206 BCE-220 CE)"),
    THREE_KINGDOMS("Three Kingdoms Period (220-280 CE)"),
    JIN("Jin Dynasty (266-420 CE)"),
    SOUTHERN_AND_NORTHERN("Southern and Northern Dynasties (420-589 CE)"),
    SUI("Sui Dynasty (581-618 CE)"),
    TANG("Tang Dynasty (618-907 CE)"),
    FIVE_DYNASTIES_TEN_KINGDOMS("Five Dynasties and Ten Kingdoms (907-960 CE)"),
    SONG("Song Dynasty (960-1279 CE)"),
    LIAO("Liao Dynasty (907-1125 CE)"),
    JIN_JURCHEN("Jin Dynasty (1115-1234 CE)"),
    YUAN("Yuan Dynasty (1271-1368 CE)"),
    MING("Ming Dynasty (1368-1644 CE)"),
    QING("Qing Dynasty (1644-1912 CE)"),
    REPUBLIC_OF_CHINA("Republic of China Period (1912-1949 CE)");

    private final String displayName;

    DynastyPeriod(String displayName) {
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
