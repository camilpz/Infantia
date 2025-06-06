package com.inf.daycare.enums;

public enum NotificationTypeEnum {
    GENERAL("General"),
    IMPORTANTE("Importante"),
    INCIDENTE("Incidente"),
    INFORMACION_GENERAL("Información General"),
    EVENTO("Evento");

    private final String displayName;

    NotificationTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
