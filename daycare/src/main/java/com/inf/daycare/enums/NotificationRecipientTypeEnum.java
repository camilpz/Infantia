package com.inf.daycare.enums;

public enum NotificationRecipientTypeEnum {
    GLOBAL_DAYCARE("Para toda la guardería"),
    PARENT("Para los padres del niño"),
    GENERAL_INFORMATION("Para información general");

    private final String displayName;

    NotificationRecipientTypeEnum(String displayName) {
        this.displayName = displayName;
    }
}
