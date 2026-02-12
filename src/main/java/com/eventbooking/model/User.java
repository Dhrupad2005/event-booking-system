package com.eventbooking.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a User in the booking system
 */
public class User {
    private final String userId;
    private String email;
    private String password; // In production, this would be hashed
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private UserRole role;
    private final LocalDateTime registeredAt;
    private final List<Booking> bookingHistory;
    private boolean isActive;
    
    public User(String email, String password, String firstName, 
                String lastName, String phoneNumber, UserRole role) {
        this.userId = UUID.randomUUID().toString();
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.registeredAt = LocalDateTime.now();
        this.bookingHistory = new ArrayList<>();
        this.isActive = true;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public void addBooking(Booking booking) {
        bookingHistory.add(booking);
    }
    
    public void deactivate() {
        this.isActive = false;
    }
    
    public void activate() {
        this.isActive = true;
    }
    
    // Getters and Setters
    public String getUserId() {
        return userId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }
    
    public List<Booking> getBookingHistory() {
        return new ArrayList<>(bookingHistory);
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
    
    @Override
    public String toString() {
        return String.format("User{id='%s', name='%s', email='%s', role=%s}",
                userId, getFullName(), email, role);
    }
}
