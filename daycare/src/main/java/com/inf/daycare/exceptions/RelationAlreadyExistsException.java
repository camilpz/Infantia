package com.inf.daycare.exceptions;

public class RelationAlreadyExistsException extends RuntimeException {
    public RelationAlreadyExistsException(String message) {
        super(message);
    }
}
