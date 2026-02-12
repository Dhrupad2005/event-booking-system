package com.eventbooking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a booking made by a user
 * Aggregates multiple tickets in a single transaction
 */
public class Booking {
    private final String bookingId;
    private final User user;
    private final Event event;
    private final List<Ticket> tickets;
    private BookingStatus status;
    private final LocalDateTime bookingDateTime;
    private BigDecimal totalAmount;
    private Payment payment;
    
    public Booking(User user, Event event) {
        this.bookingId = generateBookingReference();
        this.user = user;
        this.event = event;
        this.tickets = new ArrayList<>();
        this.status = BookingStatus.PENDING;
        this.bookingDateTime = LocalDateTime.now();
        this.totalAmount = BigDecimal.ZERO;
    }
    
    private String generateBookingReference() {
        return "BKG-" + LocalDateTime.now().getYear() + "-" + 
               UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
        totalAmount = totalAmount.add(ticket.getPricePaid());
    }
    
    public void confirm() {
        if (payment != null && payment.getStatus() == PaymentStatus.COMPLETED) {
            this.status = BookingStatus.CONFIRMED;
        }
    }
    
    public void cancel() {
        this.status = BookingStatus.CANCELLED;
        tickets.forEach(Ticket::cancel);
    }
    
    public int getTotalTickets() {
        return tickets.size();
    }
    
    public boolean canBeCancelled() {
        return status == BookingStatus.CONFIRMED && 
               LocalDateTime.now().isBefore(event.getEventDateTime().minusHours(24));
    }
    
    // Getters and Setters
    public String getBookingId() {
        return bookingId;
    }
    
    public User getUser() {
        return user;
    }
    
    public Event getEvent() {
        return event;
    }
    
    public List<Ticket> getTickets() {
        return new ArrayList<>(tickets);
    }
    
    public BookingStatus getStatus() {
        return status;
    }
    
    public void setStatus(BookingStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public Payment getPayment() {
        return payment;
    }
    
    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(bookingId, booking.bookingId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(bookingId);
    }
    
    @Override
    public String toString() {
        return String.format("Booking{id='%s', user='%s', event='%s', tickets=%d, amount=%s, status=%s}",
                bookingId, user.getFullName(), event.getName(), 
                tickets.size(), totalAmount, status);
    }
}
