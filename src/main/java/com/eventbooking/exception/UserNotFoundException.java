package com.eventbooking.exception;

public class UserNotFoundException extends BookingSystemException {
    public UserNotFoundException(String message) {
        super(message);
    }
}