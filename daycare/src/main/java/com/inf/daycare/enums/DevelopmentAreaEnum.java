package com.inf.daycare.enums;

public enum DevelopmentAreaEnum {
    COGNITIVO("Cognitivo"),
    SOCIO_EMOCIONAL("Socioemocional"),
    FISICO_MOTOR("Físico-Motor"),
    LENGUAJE("Lenguaje"),
    ADAPTATIVO("Adaptativo"),
    CREATIVO("Creativo");

    private final String displayName;

    DevelopmentAreaEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
