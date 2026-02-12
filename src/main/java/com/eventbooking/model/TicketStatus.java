package com.eventbooking.model;

/**
 * Enum representing the status of a ticket
 */
public enum TicketStatus {
    ACTIVE("Active"),
    USED("Used"),
    CANCELLED("Cancelled"),
    REFUNDED("Refunded"),
    EXPIRED("Expired");
    
    private final String displayName;
    
    TicketStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
