package com.detroitlabs.devicemanager.constants;

public enum FilterType {
    PLATFORM("PLATFORM"),
    VERSION("version"),
    SCREEN_SIZE("screenSize"),
    SCREEN_RESOLUTION("screenResolution"),
    YEAR_CLASS("yearClass"),
    IS_SAMSUNG("isSamsung");

    private final String typeText;

    FilterType(String typeText) {
        this.typeText = typeText;
    }

    @Override
    public String toString() {
        return typeText;
    }
}
