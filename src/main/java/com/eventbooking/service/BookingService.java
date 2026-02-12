package com.eventbooking.service;

import com.eventbooking.model.*;
import com.eventbooking.repository.BookingRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service layer for Booking-related business logic
 * Handles the core booking workflow
 */
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final EventService eventService;
    private final UserService userService;
    private final PaymentService paymentService;
    
    public BookingService(BookingRepository bookingRepository, 
                         EventService eventService,
                         UserService userService,
                         PaymentService paymentService) {
        this.bookingRepository = bookingRepository;
        this.eventService = eventService;
        this.userService = userService;
        this.paymentService = paymentService;
    }
    
    /**
     * Create a new booking
     * @param userId User making the booking
     * @param eventId Event to book
     * @param ticketRequests Map of TicketType ID to quantity
     * @return Created booking
     */
    public Booking createBooking(String userId, String eventId, 
                                 Map<String, Integer> ticketRequests) {
        // Validate
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);
        
        if (!event.isBookable()) {
            throw new IllegalStateException("Event is not available for booking");
        }
        
        // Create booking
        Booking booking = new Booking(user, event);
        
        // Process ticket requests
        List<Ticket> tickets = new ArrayList<>();
        for (Map.Entry<String, Integer> request : ticketRequests.entrySet()) {
            String ticketTypeId = request.getKey();
            int quantity = request.getValue();
            
            TicketType ticketType = findTicketType(event, ticketTypeId);
            
            if (!ticketType.reserveTickets(quantity)) {
                // Rollback previous reservations
                rollbackReservations(tickets);
                throw new IllegalStateException(
                    "Insufficient tickets available for " + ticketType.getName());
            }
            
            // Create tickets
            for (int i = 0; i < quantity; i++) {
                String seatNumber = generateSeatNumber(event, ticketType);
                Ticket ticket = new Ticket(event, ticketType, seatNumber, ticketType.getPrice());
                tickets.add(ticket);
                booking.addTicket(ticket);
            }
        }
        
        // Save booking
        Booking savedBooking = bookingRepository.save(booking);
        user.addBooking(savedBooking);
        
        return savedBooking;
    }
    
    /**
     * Process payment for a booking
     */
    public void processPayment(String bookingId, PaymentMethod paymentMethod) {
        Booking booking = getBookingById(bookingId);
        
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking is not in pending state");
        }
        
        Payment payment = paymentService.processPayment(booking, paymentMethod);
        booking.setPayment(payment);
        
        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            booking.confirm();
            bookingRepository.update(booking);
        } else {
            booking.setStatus(BookingStatus.FAILED);
            releaseTickets(booking);
            bookingRepository.update(booking);
            throw new IllegalStateException("Payment failed");
        }
    }
    
    /**
     * Cancel a booking
     */
    public void cancelBooking(String bookingId) {
        Booking booking = getBookingById(bookingId);
        
        if (!booking.canBeCancelled()) {
            throw new IllegalStateException(
                "Booking cannot be cancelled (must be at least 24h before event)");
        }
        
        booking.cancel();
        releaseTickets(booking);
        
        // Process refund if payment was made
        if (booking.getPayment() != null && 
            booking.getPayment().getStatus() == PaymentStatus.COMPLETED) {
            paymentService.refundPayment(booking.getPayment());
            booking.setStatus(BookingStatus.REFUNDED);
        }
        
        bookingRepository.update(booking);
    }
    
    /**
     * Get booking by ID
     */
    public Booking getBookingById(String bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));
    }
    
    /**
     * Get all bookings for a user
     */
    public List<Booking> getUserBookings(String userId) {
        return bookingRepository.findByUserId(userId);
    }
    
    /**
     * Get all bookings for an event
     */
    public List<Booking> getEventBookings(String eventId) {
        return bookingRepository.findByEventId(eventId);
    }
    
    /**
     * Get confirmed bookings for a user
     */
    public List<Booking> getConfirmedBookings(String userId) {
        return bookingRepository.findByUserIdAndStatus(userId, BookingStatus.CONFIRMED);
    }
    
    private TicketType findTicketType(Event event, String ticketTypeId) {
        return event.getTicketTypes().stream()
                .filter(tt -> tt.getTicketTypeId().equals(ticketTypeId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Ticket type not found"));
    }
    
    private void rollbackReservations(List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            ticket.getTicketType().releaseTickets(1);
        }
    }
    
    private void releaseTickets(Booking booking) {
        for (Ticket ticket : booking.getTickets()) {
            if (ticket.getStatus() != TicketStatus.USED) {
                ticket.getTicketType().releaseTickets(1);
            }
        }
    }
    
    private String generateSeatNumber(Event event, TicketType ticketType) {
        // Simplified seat number generation
        return ticketType.getTier().name() + "-" + 
               (ticketType.getBookedCount() + 1);
    }
}
