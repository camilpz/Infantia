package com.inf.daycare.enums;

public enum PayStatusEnum {
    PENDIENTE,
    ACEPTADO,
    RECHAZADO,
    CANCELADO;

    public static PayStatusEnum fromString(String status) {
        for (PayStatusEnum payStatus : PayStatusEnum.values()) {
            if (payStatus.name().equalsIgnoreCase(status)) {
                return payStatus;
            }
        }
        throw new IllegalArgumentException("No enum constant " + PayStatusEnum.class.getCanonicalName() + "." + status);
    }
}
