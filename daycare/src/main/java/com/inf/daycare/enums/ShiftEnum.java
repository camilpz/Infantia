package com.inf.daycare.enums;

public enum ShiftEnum {
    MAÑANA("Mañana"),
    TARDE("Tarde"),
    TURNO_COMPLETO("Turno completo"),
    AMBOS("Ambos");

    private final String value;

    ShiftEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    public static ShiftEnum fromValue(String value) {
        for (ShiftEnum shift : ShiftEnum.values()) {
            if (shift.getValue().equalsIgnoreCase(value)) {
                return shift;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
