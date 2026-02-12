package com.eventbooking.facade;

import com.eventbooking.model.*;
import com.eventbooking.service.BookingService;
import com.eventbooking.service.EventService;
import com.eventbooking.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Facade pattern implementation
 * Provides a simplified interface to the booking system
 */
public class BookingSystemFacade {
    
    private final UserService userService;
    private final EventService eventService;
    private final BookingService bookingService;
    
    public BookingSystemFacade(UserService userService, 
                              EventService eventService,
                              BookingService bookingService) {
        this.userService = userService;
        this.eventService = eventService;
        this.bookingService = bookingService;
    }
    
    // ===== User Operations =====
    
    public User registerUser(String email, String password, String firstName,
                           String lastName, String phoneNumber) {
        return userService.registerUser(email, password, firstName, lastName, phoneNumber);
    }
    
    public Optional<User> login(String email, String password) {
        return userService.authenticate(email, password);
    }
    
    public User getUserProfile(String userId) {
        return userService.getUserById(userId);
    }
    
    // ===== Event Operations =====
    
    public Event createEvent(String name, String description, LocalDateTime eventDateTime,
                           Venue venue, EventCategory category, int totalCapacity) {
        return eventService.createEvent(name, description, eventDateTime, 
                                       venue, category, totalCapacity);
    }
    
    public void addTicketTypeToEvent(String eventId, String name, String description,
                                    BigDecimal price, int quantity, TicketTier tier) {
        TicketType ticketType = new TicketType(name, description, price, quantity, tier);
        eventService.addTicketType(eventId, ticketType);
    }
    
    public List<Event> browseUpcomingEvents() {
        return eventService.getUpcomingEvents();
    }
    
    public List<Event> searchEvents(String keyword) {
        return eventService.searchEvents(keyword);
    }
    
    public List<Event> getEventsByCategory(EventCategory category) {
        return eventService.getEventsByCategory(category);
    }
    
    public Event getEventDetails(String eventId) {
        return eventService.getEventById(eventId);
    }
    
    public int checkEventAvailability(String eventId) {
        return eventService.getAvailableCapacity(eventId);
    }
    
    // ===== Booking Operations =====
    
    public Booking bookTickets(String userId, String eventId, 
                              Map<String, Integer> ticketRequests) {
        return bookingService.createBooking(userId, eventId, ticketRequests);
    }
    
    public void makePayment(String bookingId, PaymentMethod paymentMethod) {
        bookingService.processPayment(bookingId, paymentMethod);
    }
    
    public void cancelBooking(String bookingId) {
        bookingService.cancelBooking(bookingId);
    }
    
    public List<Booking> getUserBookingHistory(String userId) {
        return bookingService.getUserBookings(userId);
    }
    
    public List<Booking> getUpcomingBookings(String userId) {
        return bookingService.getConfirmedBookings(userId);
    }
    
    public Booking getBookingDetails(String bookingId) {
        return bookingService.getBookingById(bookingId);
    }
    
    // ===== Admin Operations =====
    
    public void cancelEvent(String eventId) {
        eventService.cancelEvent(eventId);
    }
    
    public List<Booking> getEventBookings(String eventId) {
        return bookingService.getEventBookings(eventId);
    }
    
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }
}
