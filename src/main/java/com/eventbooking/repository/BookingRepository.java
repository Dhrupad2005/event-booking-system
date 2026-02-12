package com.eventbooking.repository;

import com.eventbooking.model.Booking;
import com.eventbooking.model.BookingStatus;

import java.util.List;

/**
 * Repository interface for Booking entities
 */
public interface BookingRepository extends Repository<Booking, String> {
    
    /**
     * Find bookings by user ID
     */
    List<Booking> findByUserId(String userId);
    
    /**
     * Find bookings by event ID
     */
    List<Booking> findByEventId(String eventId);
    
    /**
     * Find bookings by status
     */
    List<Booking> findByStatus(BookingStatus status);
    
    /**
     * Find bookings by user and status
     */
    List<Booking> findByUserIdAndStatus(String userId, BookingStatus status);
}
