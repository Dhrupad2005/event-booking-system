package com.eventbooking.exception;

public class UnauthorizedException extends BookingSystemException {
    public UnauthorizedException(String message) {
        super(message);
    }
}