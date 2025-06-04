package com.inf.daycare.enums;

public enum AttendanceEnum {
    PRESENTE("Presente"),
    AUSENTE("Ausente"),
    TARDE("Llegada tarde");

    private final String displayName;

    AttendanceEnum (String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
