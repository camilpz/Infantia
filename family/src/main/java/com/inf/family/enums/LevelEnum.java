package com.inf.family.enums;

public enum LevelEnum {
//    PRE_KINDERGARTEN("Pre-Kindergarten"),
//    KINDERGARTEN("Kindergarten"),
//    FIRST_GRADE("First Grade"),
//    SECOND_GRADE("Second Grade"),
//    THIRD_GRADE("Third Grade"),
//    FOURTH_GRADE("Fourth Grade"),
//    FIFTH_GRADE("Fifth Grade"),
//    SIXTH_GRADE("Sixth Grade"),
//    SEVENTH_GRADE("Seventh Grade"),
//    EIGHTH_GRADE("Eighth Grade"),
//    NINTH_GRADE("Ninth Grade"),
//    TENTH_GRADE("Tenth Grade"),
//    ELEVENTH_GRADE("Eleventh Grade"),
//    TWELFTH_GRADE("Twelfth Grade");
//
//    private final String level;
//
//    LevelEnum(String level) {
//        this.level = level;
//    }
//
//    public String getLevel() {
//        return level;
//    }
    MUY_MALO("Muy malo"),
    MALO("Malo"),
    REGULAR("Regular"),
    BUENO("Bueno"),
    MUY_BUENO("Muy bueno");

    private final String level;

    LevelEnum(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }
}
