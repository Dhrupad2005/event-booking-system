package com.eventbooking.exception;

public class PaymentFailedException extends BookingSystemException {
    public PaymentFailedException(String message) {
        super(message);
    }
}