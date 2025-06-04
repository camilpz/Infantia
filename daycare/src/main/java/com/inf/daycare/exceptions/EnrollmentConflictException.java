package com.inf.daycare.exceptions;

public class EnrollmentConflictException extends RuntimeException {
    public EnrollmentConflictException(String message) {
        super(message);
    }
}
