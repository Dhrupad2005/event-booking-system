package com.eventbooking.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic Repository interface
 * Defines standard CRUD operations
 * 
 * @param <T> The entity type
 * @param <ID> The ID type
 */
public interface Repository<T, ID> {
    
    /**
     * Save an entity
     */
    T save(T entity);
    
    /**
     * Find entity by ID
     */
    Optional<T> findById(ID id);
    
    /**
     * Find all entities
     */
    List<T> findAll();
    
    /**
     * Update an entity
     */
    T update(T entity);
    
    /**
     * Delete entity by ID
     */
    boolean deleteById(ID id);
    
    /**
     * Check if entity exists
     */
    boolean existsById(ID id);
}
