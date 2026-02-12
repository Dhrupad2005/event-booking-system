package com.eventbooking.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a type/tier of ticket for an event (e.g., VIP, General, Early Bird)
 */
public class TicketType {
    private final String ticketTypeId;
    private String name;
    private String description;
    private BigDecimal price;
    private int totalQuantity;
    private int bookedCount;
    private TicketTier tier;
    
    public TicketType(String name, String description, BigDecimal price, 
                      int totalQuantity, TicketTier tier) {
        this.ticketTypeId = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.price = price;
        this.totalQuantity = totalQuantity;
        this.bookedCount = 0;
        this.tier = tier;
    }
    
    public boolean isAvailable() {
        return bookedCount < totalQuantity;
    }
    
    public int getAvailableQuantity() {
        return totalQuantity - bookedCount;
    }
    
    public synchronized boolean reserveTickets(int quantity) {
        if (getAvailableQuantity() >= quantity) {
            bookedCount += quantity;
            return true;
        }
        return false;
    }
    
    public synchronized void releaseTickets(int quantity) {
        bookedCount = Math.max(0, bookedCount - quantity);
    }
    
    // Getters and Setters
    public String getTicketTypeId() {
        return ticketTypeId;
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
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public int getTotalQuantity() {
        return totalQuantity;
    }
    
    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
    
    public int getBookedCount() {
        return bookedCount;
    }
    
    public TicketTier getTier() {
        return tier;
    }
    
    public void setTier(TicketTier tier) {
        this.tier = tier;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketType that = (TicketType) o;
        return Objects.equals(ticketTypeId, that.ticketTypeId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(ticketTypeId);
    }
    
    @Override
    public String toString() {
        return String.format("TicketType{name='%s', price=%s, available=%d/%d, tier=%s}",
                name, price, getAvailableQuantity(), totalQuantity, tier);
    }
}
