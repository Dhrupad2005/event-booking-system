package com.eventbooking.model;

/**
 * Enum representing different categories of events
 */
public enum EventCategory {
    CONCERT("Concert"),
    SPORTS("Sports"),
    THEATER("Theater"),
    CONFERENCE("Conference"),
    WORKSHOP("Workshop"),
    FESTIVAL("Festival"),
    EXHIBITION("Exhibition"),
    COMEDY("Comedy"),
    OTHER("Other");
    
    private final String displayName;
    
    EventCategory(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
