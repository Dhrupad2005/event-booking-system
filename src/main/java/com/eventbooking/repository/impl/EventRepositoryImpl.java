package com.eventbooking.repository.impl;

import com.eventbooking.model.Event;
import com.eventbooking.model.EventCategory;
import com.eventbooking.model.EventStatus;
import com.eventbooking.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of EventRepository
 * Thread-safe using ConcurrentHashMap
 */
public class EventRepositoryImpl implements EventRepository {
    
    private final Map<String, Event> events = new ConcurrentHashMap<>();
    
    @Override
    public Event save(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        events.put(event.getEventId(), event);
        return event;
    }
    
    @Override
    public Optional<Event> findById(String id) {
        return Optional.ofNullable(events.get(id));
    }
    
    @Override
    public List<Event> findAll() {
        return new ArrayList<>(events.values());
    }
    
    @Override
    public Event update(Event event) {
        if (event == null || !events.containsKey(event.getEventId())) {
            throw new IllegalArgumentException("Event not found");
        }
        events.put(event.getEventId(), event);
        return event;
    }
    
    @Override
    public boolean deleteById(String id) {
        return events.remove(id) != null;
    }
    
    @Override
    public boolean existsById(String id) {
        return events.containsKey(id);
    }
    
    @Override
    public List<Event> findByCategory(EventCategory category) {
        return events.values().stream()
                .filter(event -> event.getCategory() == category)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Event> findByStatus(EventStatus status) {
        return events.values().stream()
                .filter(event -> event.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Event> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return events.values().stream()
                .filter(event -> !event.getEventDateTime().isBefore(startDate) &&
                               !event.getEventDateTime().isAfter(endDate))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Event> findUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        return events.values().stream()
                .filter(event -> event.getEventDateTime().isAfter(now) &&
                               event.getStatus() == EventStatus.UPCOMING)
                .sorted(Comparator.comparing(Event::getEventDateTime))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Event> searchByName(String name) {
        String searchTerm = name.toLowerCase();
        return events.values().stream()
                .filter(event -> event.getName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }
}
