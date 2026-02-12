package com.eventbooking.exception;

public class EventNotFoundException extends BookingSystemException {
    public EventNotFoundException(String message) {
        super(message);
    }
}