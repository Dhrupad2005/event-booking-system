package com.eventbooking.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a Venue where events are held
 */
public class Venue {
    private final String venueId;
    private String name;
    private Address address;
    private int capacity;
    private String facilities;
    
    public Venue(String name, Address address, int capacity, String facilities) {
        this.venueId = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.facilities = facilities;
    }
    
    // Getters and Setters
    public String getVenueId() {
        return venueId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    public String getFacilities() {
        return facilities;
    }
    
    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venue venue = (Venue) o;
        return Objects.equals(venueId, venue.venueId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(venueId);
    }
    
    @Override
    public String toString() {
        return String.format("Venue{name='%s', address=%s, capacity=%d}",
                name, address, capacity);
    }
}
