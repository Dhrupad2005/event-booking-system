package com.eventbooking.exception;

public class DuplicateEmailException extends BookingSystemException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}