package com.eventbooking.exception;

public class InsufficientTicketsException extends BookingSystemException {
    public InsufficientTicketsException(String message) {
        super(message);
    }
}