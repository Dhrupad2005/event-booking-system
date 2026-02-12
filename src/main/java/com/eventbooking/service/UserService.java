package com.eventbooking.service;

import com.eventbooking.model.User;
import com.eventbooking.model.UserRole;
import com.eventbooking.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for User-related business logic
 */
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Register a new user
     */
    public User registerUser(String email, String password, String firstName,
                            String lastName, String phoneNumber) {
        validateUserData(email, password, firstName, lastName);
        
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }
        
        User user = new User(email, hashPassword(password), firstName, 
                           lastName, phoneNumber, UserRole.CUSTOMER);
        return userRepository.save(user);
    }
    
    /**
     * Authenticate user
     */
    public Optional<User> authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (verifyPassword(password, user.getPassword()) && user.isActive()) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
    
    /**
     * Get user by ID
     */
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }
    
    /**
     * Get user by email
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Update user profile
     */
    public User updateUser(User user) {
        if (!userRepository.existsById(user.getUserId())) {
            throw new IllegalArgumentException("User not found: " + user.getUserId());
        }
        return userRepository.update(user);
    }
    
    /**
     * Change user password
     */
    public void changePassword(String userId, String oldPassword, String newPassword) {
        User user = getUserById(userId);
        
        if (!verifyPassword(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid old password");
        }
        
        validatePassword(newPassword);
        user.setPassword(hashPassword(newPassword));
        userRepository.update(user);
    }
    
    /**
     * Deactivate user account
     */
    public void deactivateUser(String userId) {
        User user = getUserById(userId);
        user.deactivate();
        userRepository.update(user);
    }
    
    /**
     * Activate user account
     */
    public void activateUser(String userId) {
        User user = getUserById(userId);
        user.activate();
        userRepository.update(user);
    }
    
    private void validateUserData(String email, String password, String firstName, String lastName) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        validatePassword(password);
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
    }
    
    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
    }
    
    // Simplified password hashing (in production, use BCrypt or similar)
    private String hashPassword(String password) {
        return "HASHED_" + password; // Replace with actual hashing
    }
    
    private boolean verifyPassword(String rawPassword, String hashedPassword) {
        return hashPassword(rawPassword).equals(hashedPassword);
    }
}
