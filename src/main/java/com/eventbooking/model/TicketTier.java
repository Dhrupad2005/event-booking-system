package com.eventbooking.model;

/**
 * Enum representing different tiers of tickets
 */
public enum TicketTier {
    VIP("VIP", 1),
    PREMIUM("Premium", 2),
    STANDARD("Standard", 3),
    ECONOMY("Economy", 4),
    EARLY_BIRD("Early Bird", 5);
    
    private final String displayName;
    private final int priority;
    
    TicketTier(String displayName, int priority) {
        this.displayName = displayName;
        this.priority = priority;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getPriority() {
        return priority;
    }
}
