package com.inf.family.exceptions;

public class RelationAlreadyExistsException extends RuntimeException {
    public RelationAlreadyExistsException(String message) {
        super(message);
    }
}
