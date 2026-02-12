package com.eventbooking.model;

/**
 * Enum representing different user roles in the system
 */
public enum UserRole {
    ADMIN("Administrator"),
    EVENT_ORGANIZER("Event Organizer"),
    CUSTOMER("Customer");
    
    private final String displayName;
    
    UserRole(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
