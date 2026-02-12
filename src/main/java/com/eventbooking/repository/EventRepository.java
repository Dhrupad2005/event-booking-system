package com.eventbooking.repository;

import com.eventbooking.model.Event;
import com.eventbooking.model.EventCategory;
import com.eventbooking.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Event entities
 */
public interface EventRepository extends Repository<Event, String> {
    
    /**
     * Find events by category
     */
    List<Event> findByCategory(EventCategory category);
    
    /**
     * Find events by status
     */
    List<Event> findByStatus(EventStatus status);
    
    /**
     * Find events by date range
     */
    List<Event> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find upcoming events
     */
    List<Event> findUpcomingEvents();
    
    /**
     * Search events by name
     */
    List<Event> searchByName(String name);
}
