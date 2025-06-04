package com.inf.daycare.exceptions;

public class CommentLimitException extends RuntimeException {
    public CommentLimitException(String message) {
        super(message);
    }
}
