package com.eventbooking.repository;

import com.eventbooking.model.User;

import java.util.Optional;

/**
 * Repository interface for User entities
 */
public interface UserRepository extends Repository<User, String> {
    
    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
}
