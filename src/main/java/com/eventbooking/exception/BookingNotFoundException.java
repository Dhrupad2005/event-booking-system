package com.eventbooking.exception;

public class BookingNotFoundException extends BookingSystemException {
    public BookingNotFoundException(String message) {
        super(message);
    }
}