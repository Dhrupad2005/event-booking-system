package com.eventbooking.service;

import com.eventbooking.exception.EventNotFoundException;
import com.eventbooking.model.*;
import com.eventbooking.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for Event-related business logic
 * Implements business rules and validation
 */
public class EventService {
    
    private final EventRepository eventRepository;
    
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    
    /**
     * Create a new event
     */
    public Event createEvent(String name, String description, LocalDateTime eventDateTime,
                            Venue venue, EventCategory category, int totalCapacity) {
        validateEventData(name, eventDateTime, totalCapacity);
        
        Event event = new Event(name, description, eventDateTime, venue, category, totalCapacity);
        return eventRepository.save(event);
    }
    
    /**
     * Add a ticket type to an event
     */
    public void addTicketType(String eventId, TicketType ticketType) {
        Event event = getEventById(eventId);
        event.addTicketType(ticketType);
        eventRepository.update(event);
    }
    
    /**
     * Get event by ID
     */
    public Event getEventById(String eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found: " + eventId));
    }
    
    /**
     * Get all events
     */
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
    
    /**
     * Get upcoming events
     */
    public List<Event> getUpcomingEvents() {
        return eventRepository.findUpcomingEvents();
    }
    
    /**
     * Search events by name
     */
    public List<Event> searchEvents(String name) {
        return eventRepository.searchByName(name);
    }
    
    /**
     * Get events by category
     */
    public List<Event> getEventsByCategory(EventCategory category) {
        return eventRepository.findByCategory(category);
    }
    
    /**
     * Get events by date range
     */
    public List<Event> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return eventRepository.findByDateRange(startDate, endDate);
    }
    
    /**
     * Cancel an event
     */
    public void cancelEvent(String eventId) {
        Event event = getEventById(eventId);
        event.setStatus(EventStatus.CANCELLED);
        eventRepository.update(event);
    }
    
    /**
     * Update event details
     */
    public Event updateEvent(Event event) {
        if (!eventRepository.existsById(event.getEventId())) {
            throw new EventNotFoundException("Event not found: " + event.getEventId());
        }
        return eventRepository.update(event);
    }
    
    /**
     * Check if event is bookable
     */
    public boolean isEventBookable(String eventId) {
        Event event = getEventById(eventId);
        return event.isBookable();
    }
    
    /**
     * Get available capacity for an event
     */
    public int getAvailableCapacity(String eventId) {
        Event event = getEventById(eventId);
        return event.getAvailableCapacity();
    }
    
    private void validateEventData(String name, LocalDateTime eventDateTime, int capacity) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Event name cannot be empty");
        }
        if (eventDateTime == null || eventDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Event date must be in the future");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Event capacity must be positive");
        }
    }
}
