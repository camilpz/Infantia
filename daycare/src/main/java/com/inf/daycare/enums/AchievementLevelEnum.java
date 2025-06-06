package com.inf.daycare.enums;

public enum AchievementLevelEnum {
    NO_INICIADO("No Iniciado"),
    EN_PROGRESO("En Progreso"),
    LOGRADO("Logrado"),
    SUPERA_EXPECTATIVAS("Supera Expectativas");

    private final String displayName;

    AchievementLevelEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
