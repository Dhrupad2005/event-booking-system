package com.eventbooking.repository.impl;

import com.eventbooking.model.Booking;
import com.eventbooking.model.BookingStatus;
import com.eventbooking.repository.BookingRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of BookingRepository
 */
public class BookingRepositoryImpl implements BookingRepository {
    
    private final Map<String, Booking> bookings = new ConcurrentHashMap<>();
    
    @Override
    public Booking save(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }
        bookings.put(booking.getBookingId(), booking);
        return booking;
    }
    
    @Override
    public Optional<Booking> findById(String id) {
        return Optional.ofNullable(bookings.get(id));
    }
    
    @Override
    public List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }
    
    @Override
    public Booking update(Booking booking) {
        if (booking == null || !bookings.containsKey(booking.getBookingId())) {
            throw new IllegalArgumentException("Booking not found");
        }
        bookings.put(booking.getBookingId(), booking);
        return booking;
    }
    
    @Override
    public boolean deleteById(String id) {
        return bookings.remove(id) != null;
    }
    
    @Override
    public boolean existsById(String id) {
        return bookings.containsKey(id);
    }
    
    @Override
    public List<Booking> findByUserId(String userId) {
        return bookings.values().stream()
                .filter(booking -> booking.getUser().getUserId().equals(userId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Booking> findByEventId(String eventId) {
        return bookings.values().stream()
                .filter(booking -> booking.getEvent().getEventId().equals(eventId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Booking> findByStatus(BookingStatus status) {
        return bookings.values().stream()
                .filter(booking -> booking.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Booking> findByUserIdAndStatus(String userId, BookingStatus status) {
        return bookings.values().stream()
                .filter(booking -> booking.getUser().getUserId().equals(userId) &&
                                 booking.getStatus() == status)
                .collect(Collectors.toList());
    }
}
