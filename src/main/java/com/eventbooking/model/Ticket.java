package com.eventbooking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an individual ticket instance
 */
public class Ticket {
    private final String ticketId;
    private final Event event;
    private final TicketType ticketType;
    private final String seatNumber;
    private TicketStatus status;
    private final LocalDateTime issuedAt;
    private BigDecimal pricePaid;
    
    public Ticket(Event event, TicketType ticketType, String seatNumber, BigDecimal pricePaid) {
        this.ticketId = generateTicketNumber();
        this.event = event;
        this.ticketType = ticketType;
        this.seatNumber = seatNumber;
        this.status = TicketStatus.ACTIVE;
        this.issuedAt = LocalDateTime.now();
        this.pricePaid = pricePaid;
    }
    
    private String generateTicketNumber() {
        return "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    public void cancel() {
        this.status = TicketStatus.CANCELLED;
    }
    
    public void use() {
        if (status == TicketStatus.ACTIVE) {
            this.status = TicketStatus.USED;
        }
    }
    
    public boolean isValid() {
        return status == TicketStatus.ACTIVE && 
               LocalDateTime.now().isBefore(event.getEventDateTime());
    }
    
    // Getters
    public String getTicketId() {
        return ticketId;
    }
    
    public Event getEvent() {
        return event;
    }
    
    public TicketType getTicketType() {
        return ticketType;
    }
    
    public String getSeatNumber() {
        return seatNumber;
    }
    
    public TicketStatus getStatus() {
        return status;
    }
    
    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }
    
    public BigDecimal getPricePaid() {
        return pricePaid;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(ticketId, ticket.ticketId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(ticketId);
    }
    
    @Override
    public String toString() {
        return String.format("Ticket{id='%s', event='%s', type='%s', seat='%s', status=%s}",
                ticketId, event.getName(), ticketType.getName(), seatNumber, status);
    }
}
