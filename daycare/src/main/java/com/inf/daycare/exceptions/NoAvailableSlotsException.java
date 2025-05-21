package com.inf.daycare.exceptions;

public class NoAvailableSlotsException extends RuntimeException {
    public NoAvailableSlotsException(String message) { super(message); }
}
