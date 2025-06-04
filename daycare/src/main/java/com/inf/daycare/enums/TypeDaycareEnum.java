package com.inf.daycare.enums;

public enum TypeDaycareEnum {
    PUBLICA("Publica"),
    PRIVADA("Privada"),
    SEMI_PRIVADA("Semi-Privada");

    private final String value;

    TypeDaycareEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
