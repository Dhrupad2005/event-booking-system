# Object-Oriented Programming Principles Guide

This document explains how OOP principles are applied in the Event Ticket Booking System.

---

## Table of Contents
1. [Four Pillars of OOP](#four-pillars-of-oop)
2. [SOLID Principles](#solid-principles)
3. [Design Patterns](#design-patterns)
4. [Best Practices](#best-practices)

---

## Four Pillars of OOP

### 1. Encapsulation

**Definition**: Bundling data and methods that operate on that data within a single unit (class), and restricting direct access to internal state.

#### Examples from the Project:

**Event.java**
```java
public class Event {
    // Private fields - hidden from outside
    private final String eventId;
    private String name;
    private List<TicketType> ticketTypes;
    
    // Controlled access through public methods
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    // Business logic encapsulated within the class
    public boolean isBookable() {
        return status == EventStatus.UPCOMING && 
               LocalDateTime.now().isBefore(eventDateTime);
    }
}
```

**TicketType.java - Thread-safe encapsulation**
```java
public class TicketType {
    private int bookedCount;  // Protected state
    
    // Synchronized method ensures thread-safety
    public synchronized boolean reserveTickets(int quantity) {
        if (getAvailableQuantity() >= quantity) {
            bookedCount += quantity;
            return true;
        }
        return false;
    }
}
```

**Benefits**:
- Data integrity maintained
- Internal implementation can change without affecting clients
- Thread-safety ensured
- Business rules enforced

---

### 2. Inheritance

**Definition**: Mechanism where a new class derives properties and behaviors from an existing class.

#### Examples from the Project:

**Exception Hierarchy**
```java
// Base class
public class BookingSystemException extends RuntimeException {
    public BookingSystemException(String message) {
        super(message);
    }
}

// Derived classes inherit from base
public class EventNotFoundException extends BookingSystemException {
    public EventNotFoundException(String message) {
        super(message);
    }
}

public class InsufficientTicketsException extends BookingSystemException {
    public InsufficientTicketsException(String message) {
        super(message);
    }
}
```

**Repository Interface Hierarchy**
```java
// Generic base interface
public interface Repository<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
}

// Specialized interface inherits base methods
public interface EventRepository extends Repository<Event, String> {
    // Additional event-specific methods
    List<Event> findByCategory(EventCategory category);
    List<Event> findUpcomingEvents();
}
```

**Benefits**:
- Code reuse
- Hierarchical classification
- Extensibility
- Polymorphic behavior

---

### 3. Polymorphism

**Definition**: Ability of objects to take multiple forms, allowing different implementations to be used interchangeably.

#### Examples from the Project:

**Repository Polymorphism**
```java
// Interface defines contract
public interface EventRepository extends Repository<Event, String> {
    List<Event> findByCategory(EventCategory category);
}

// In-memory implementation
public class EventRepositoryImpl implements EventRepository {
    private final Map<String, Event> events = new ConcurrentHashMap<>();
    
    @Override
    public List<Event> findByCategory(EventCategory category) {
        return events.values().stream()
            .filter(event -> event.getCategory() == category)
            .collect(Collectors.toList());
    }
}

// Could have database implementation
public class EventRepositoryJpaImpl implements EventRepository {
    @Override
    public List<Event> findByCategory(EventCategory category) {
        // JPA/Hibernate implementation
        return entityManager.createQuery(
            "SELECT e FROM Event e WHERE e.category = :category", Event.class)
            .setParameter("category", category)
            .getResultList();
    }
}

// Usage - works with any implementation
public class EventService {
    private final EventRepository repository;  // Interface type
    
    public EventService(EventRepository repository) {
        this.repository = repository;  // Any implementation works
    }
    
    public List<Event> getEventsByCategory(EventCategory category) {
        return repository.findByCategory(category);
    }
}
```

**Enum Polymorphism**
```java
public enum PaymentMethod {
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    PAYPAL("PayPal"),
    UPI("UPI");
    
    private final String displayName;
    
    // All instances share same behavior but different data
    public String getDisplayName() {
        return displayName;
    }
}
```

**Benefits**:
- Flexible and extensible code
- Easy to add new implementations
- Cleaner client code
- Interface-based programming

---

### 4. Abstraction

**Definition**: Hiding complex implementation details and showing only essential features.

#### Examples from the Project:

**Repository Interface (Data Access Abstraction)**
```java
// Abstract interface - clients don't know about storage
public interface UserRepository extends Repository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}

// Client code doesn't care if it's in-memory, database, or cloud
public class UserService {
    private final UserRepository userRepository;
    
    public User registerUser(String email, String password, ...) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email exists");
        }
        User user = new User(email, password, ...);
        return userRepository.save(user);
    }
}
```

**Facade Pattern (System Abstraction)**
```java
// Complex subsystem abstracted behind simple interface
public class BookingSystemFacade {
    // Multiple services coordinated internally
    private final UserService userService;
    private final EventService eventService;
    private final BookingService bookingService;
    
    // Simple method hiding complex workflow
    public Booking bookTickets(String userId, String eventId, 
                              Map<String, Integer> ticketRequests) {
        // Internally coordinates multiple services
        return bookingService.createBooking(userId, eventId, ticketRequests);
    }
}
```

**Benefits**:
- Reduces complexity
- Improves maintainability
- Clear separation of concerns
- Easier testing

---

## SOLID Principles

### S - Single Responsibility Principle (SRP)

**Definition**: A class should have only one reason to change.

#### Examples:

**✓ Good - Each service has one responsibility**
```java
// EventService - Only manages events
public class EventService {
    public Event createEvent(...) { }
    public List<Event> getAllEvents() { }
    public void cancelEvent(String eventId) { }
}

// UserService - Only manages users
public class UserService {
    public User registerUser(...) { }
    public Optional<User> authenticate(...) { }
    public void changePassword(...) { }
}

// PaymentService - Only handles payments
public class PaymentService {
    public Payment processPayment(...) { }
    public void refundPayment(...) { }
}
```

**✗ Bad - Multiple responsibilities**
```java
// Don't do this!
public class EventManager {
    public Event createEvent() { }      // Event management
    public User registerUser() { }      // User management
    public Payment processPayment() { } // Payment processing
    public void sendEmail() { }         // Email notifications
}
```

---

### O - Open/Closed Principle (OCP)

**Definition**: Classes should be open for extension but closed for modification.

#### Examples:

**✓ Good - Extension through interface**
```java
// Base interface - closed for modification
public interface Repository<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
}

// Extended through new implementations - no modification needed
public class InMemoryRepository implements Repository<Event, String> {
    // In-memory implementation
}

public class DatabaseRepository implements Repository<Event, String> {
    // Database implementation
}

public class CloudRepository implements Repository<Event, String> {
    // Cloud storage implementation
}
```

**Enum Extension**
```java
// Easy to add new payment methods without modifying existing code
public enum PaymentMethod {
    CREDIT_CARD,
    DEBIT_CARD,
    PAYPAL,
    UPI,
    // NEW_METHOD  ← Simply add here
}
```

---

### L - Liskov Substitution Principle (LSP)

**Definition**: Objects of a superclass should be replaceable with objects of a subclass without breaking the application.

#### Examples:

**✓ Good - Substitutable implementations**
```java
public void testRepository(Repository<Event, String> repository) {
    Event event = new Event(...);
    
    // Works with ANY repository implementation
    repository.save(event);
    Optional<Event> found = repository.findById(event.getEventId());
}

// All these work perfectly
testRepository(new EventRepositoryImpl());
testRepository(new EventRepositoryJpaImpl());
testRepository(new EventRepositoryMongoImpl());
```

---

### I - Interface Segregation Principle (ISP)

**Definition**: Clients should not be forced to depend on interfaces they don't use.

#### Examples:

**✓ Good - Specific interfaces**
```java
// Segregated interfaces - use only what you need
public interface EventRepository extends Repository<Event, String> {
    List<Event> findByCategory(EventCategory category);
    List<Event> findUpcomingEvents();
}

public interface UserRepository extends Repository<User, String> {
    Optional<User> findByEmail(String email);
}

public interface BookingRepository extends Repository<Booking, String> {
    List<Booking> findByUserId(String userId);
    List<Booking> findByEventId(String eventId);
}
```

**✗ Bad - Fat interface**
```java
// Don't do this!
public interface Repository<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    Optional<T> findByEmail(String email);      // Not all entities have email!
    List<T> findByCategory(Category category);   // Not all have category!
    List<T> findUpcoming();                      // Not all are time-based!
}
```

---

### D - Dependency Inversion Principle (DIP)

**Definition**: High-level modules should not depend on low-level modules. Both should depend on abstractions.

#### Examples:

**✓ Good - Depend on abstractions**
```java
// High-level module depends on abstraction
public class BookingService {
    private final BookingRepository repository;  // Interface, not implementation
    private final EventService eventService;
    private final UserService userService;
    
    public BookingService(BookingRepository repository,
                         EventService eventService,
                         UserService userService) {
        this.repository = repository;
        this.eventService = eventService;
        this.userService = userService;
    }
}

// Factory handles concrete implementations
public class BookingSystemFactory {
    public static BookingSystemFacade createBookingSystem() {
        BookingRepository repository = new BookingRepositoryImpl();
        // Can easily swap implementations
        // BookingRepository repository = new BookingRepositoryJpaImpl();
        
        BookingService service = new BookingService(repository, ...);
        return new BookingSystemFacade(...);
    }
}
```

---

## Design Patterns

### 1. Repository Pattern

**Purpose**: Separate data access logic from business logic.

```java
// Abstraction layer
public interface EventRepository {
    Event save(Event event);
    Optional<Event> findById(String id);
}

// Implementation can change without affecting business logic
public class EventRepositoryImpl implements EventRepository {
    private final Map<String, Event> storage = new ConcurrentHashMap<>();
    
    public Event save(Event event) {
        storage.put(event.getEventId(), event);
        return event;
    }
}
```

### 2. Facade Pattern

**Purpose**: Provide simplified interface to complex subsystem.

```java
public class BookingSystemFacade {
    // Hides complexity of multiple services
    private final UserService userService;
    private final EventService eventService;
    private final BookingService bookingService;
    
    // Simple method for complex operation
    public Booking bookTickets(String userId, String eventId, 
                              Map<String, Integer> tickets) {
        // Coordinates multiple services internally
        return bookingService.createBooking(userId, eventId, tickets);
    }
}
```

### 3. Factory Pattern

**Purpose**: Centralize object creation and dependency management.

```java
public class BookingSystemFactory {
    public static BookingSystemFacade createBookingSystem() {
        // Create dependencies
        UserRepository userRepo = new UserRepositoryImpl();
        EventRepository eventRepo = new EventRepositoryImpl();
        
        // Wire dependencies
        UserService userService = new UserService(userRepo);
        EventService eventService = new EventService(eventRepo);
        
        // Return fully configured system
        return new BookingSystemFacade(userService, eventService, ...);
    }
}
```

### 4. Value Object Pattern

**Purpose**: Immutable objects representing values without identity.

```java
public class Address {
    private final String street;  // All fields final
    private final String city;
    private final String state;
    
    // No setters - immutable
    public Address(String street, String city, String state) {
        this.street = street;
        this.city = city;
        this.state = state;
    }
    
    // Equality based on values, not identity
    public boolean equals(Object o) {
        // Compare all fields
    }
}
```

---

## Best Practices Applied

### 1. Immutability Where Appropriate
```java
// Value object - immutable
public class Address {
    private final String street;  // final fields
    // No setters
}

// Entity - mutable but controlled
public class Event {
    private final String eventId;  // ID is immutable
    private String name;           // Name can change
    
    public void setName(String name) {  // Controlled mutation
        this.name = name;
    }
}
```

### 2. Constructor Validation
```java
public class Event {
    public Event(String name, LocalDateTime eventDateTime, int capacity) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name required");
        }
        if (eventDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Date must be in future");
        }
        this.name = name;
        this.eventDateTime = eventDateTime;
    }
}
```

### 3. Defensive Copying
```java
public class Event {
    private final List<TicketType> ticketTypes = new ArrayList<>();
    
    // Return copy, not internal list
    public List<TicketType> getTicketTypes() {
        return new ArrayList<>(ticketTypes);
    }
}
```

### 4. Thread Safety
```java
public class TicketType {
    private int bookedCount;
    
    public synchronized boolean reserveTickets(int quantity) {
        // Thread-safe critical section
        if (getAvailableQuantity() >= quantity) {
            bookedCount += quantity;
            return true;
        }
        return false;
    }
}

public class EventRepositoryImpl {
    // Thread-safe collection
    private final Map<String, Event> events = new ConcurrentHashMap<>();
}
```

### 5. Fail-Fast Validation
```java
public class BookingService {
    public Booking createBooking(String userId, String eventId, ...) {
        // Validate early
        User user = userService.getUserById(userId);  // Fails if not found
        Event event = eventService.getEventById(eventId);
        
        if (!event.isBookable()) {
            throw new InvalidBookingException("Event not bookable");
        }
        
        // Proceed with booking
    }
}
```

---

## Summary

The Event Ticket Booking System demonstrates:

1. **Encapsulation**: Private fields, public methods, controlled access
2. **Inheritance**: Exception hierarchy, interface extensions
3. **Polymorphism**: Interface implementations, enum behaviors
4. **Abstraction**: Repository interfaces, Facade pattern
5. **SOLID Principles**: Every principle demonstrated with examples
6. **Design Patterns**: Repository, Facade, Factory, Value Object
7. **Best Practices**: Immutability, validation, thread-safety, defensive coding

These principles create a maintainable, extensible, and professional codebase suitable for enterprise applications.
