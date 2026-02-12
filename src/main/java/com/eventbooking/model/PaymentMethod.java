package com.eventbooking.model;

/**
 * Enum representing different payment methods
 */
public enum PaymentMethod {
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    PAYPAL("PayPal"),
    UPI("UPI"),
    NET_BANKING("Net Banking"),
    WALLET("Digital Wallet");
    
    private final String displayName;
    
    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
