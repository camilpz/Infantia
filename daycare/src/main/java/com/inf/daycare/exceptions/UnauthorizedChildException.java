package com.inf.daycare.exceptions;

public class UnauthorizedChildException extends RuntimeException {
    public UnauthorizedChildException(String message) {
        super(message);
    }
}
