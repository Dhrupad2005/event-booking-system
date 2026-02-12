package com.eventbooking.repository.impl;

import com.eventbooking.model.User;
import com.eventbooking.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of UserRepository
 */
public class UserRepositoryImpl implements UserRepository {
    
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, String> emailToIdMap = new ConcurrentHashMap<>();
    
    @Override
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        users.put(user.getUserId(), user);
        emailToIdMap.put(user.getEmail().toLowerCase(), user.getUserId());
        return user;
    }
    
    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }
    
    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
    
    @Override
    public User update(User user) {
        if (user == null || !users.containsKey(user.getUserId())) {
            throw new IllegalArgumentException("User not found");
        }
        users.put(user.getUserId(), user);
        emailToIdMap.put(user.getEmail().toLowerCase(), user.getUserId());
        return user;
    }
    
    @Override
    public boolean deleteById(String id) {
        User user = users.remove(id);
        if (user != null) {
            emailToIdMap.remove(user.getEmail().toLowerCase());
            return true;
        }
        return false;
    }
    
    @Override
    public boolean existsById(String id) {
        return users.containsKey(id);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        String userId = emailToIdMap.get(email.toLowerCase());
        return userId != null ? Optional.ofNullable(users.get(userId)) : Optional.empty();
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return emailToIdMap.containsKey(email.toLowerCase());
    }
}
