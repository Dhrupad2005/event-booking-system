package com.eventbooking.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an Event in the booking system
 * Encapsulates all event-related information
 */
public class Event {
    private final String eventId;
    private String name;
    private String description;
    private LocalDateTime eventDateTime;
    private Venue venue;
    private EventCategory category;
    private EventStatus status;
    private final List<TicketType> ticketTypes;
    private int totalCapacity;
    
    public Event(String name, String description, LocalDateTime eventDateTime, 
                 Venue venue, EventCategory category, int totalCapacity) {
        this.eventId = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.eventDateTime = eventDateTime;
        this.venue = venue;
        this.category = category;
        this.status = EventStatus.UPCOMING;
        this.ticketTypes = new ArrayList<>();
        this.totalCapacity = totalCapacity;
    }
    
    public void addTicketType(TicketType ticketType) {
        this.ticketTypes.add(ticketType);
    }
    
    public boolean isBookable() {
        return status == EventStatus.UPCOMING && 
               LocalDateTime.now().isBefore(eventDateTime);
    }
    
    public int getAvailableCapacity() {
        int bookedTickets = ticketTypes.stream()
            .mapToInt(TicketType::getBookedCount)
            .sum();
        return totalCapacity - bookedTickets;
    }
    
    // Getters and Setters
    public String getEventId() {
        return eventId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getEventDateTime() {
        return eventDateTime;
    }
    
    public void setEventDateTime(LocalDateTime eventDateTime) {
        this.eventDateTime = eventDateTime;
    }
    
    public Venue getVenue() {
        return venue;
    }
    
    public void setVenue(Venue venue) {
        this.venue = venue;
    }
    
    public EventCategory getCategory() {
        return category;
    }
    
    public void setCategory(EventCategory category) {
        this.category = category;
    }
    
    public EventStatus getStatus() {
        return status;
    }
    
    public void setStatus(EventStatus status) {
        this.status = status;
    }
    
    public List<TicketType> getTicketTypes() {
        return new ArrayList<>(ticketTypes);
    }
    
    public int getTotalCapacity() {
        return totalCapacity;
    }
    
    public void setTotalCapacity(int totalCapacity) {
        this.totalCapacity = totalCapacity;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventId, event.eventId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }
    
    @Override
    public String toString() {
        return String.format("Event{id='%s', name='%s', date=%s, venue=%s, status=%s}",
                eventId, name, eventDateTime, venue.getName(), status);
    }
}
