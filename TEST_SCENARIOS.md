# Test Scenarios and Use Cases

This document outlines various test scenarios and use cases for the Event Ticket Booking System.

---

## User Registration & Authentication

### Scenario 1: Successful User Registration
```java
BookingSystemFacade system = BookingSystemFactory.createBookingSystem();

User user = system.registerUser(
    "alice@example.com",
    "SecurePass123",
    "Alice",
    "Johnson",
    "+1-555-0100"
);

// Expected: User object created with CUSTOMER role
// user.getUserId() != null
// user.getRole() == UserRole.CUSTOMER
// user.isActive() == true
```

### Scenario 2: Duplicate Email Registration
```java
system.registerUser("alice@example.com", "Pass123", "Alice", "J", "+15550100");

// Try to register again with same email
try {
    system.registerUser("alice@example.com", "Pass456", "Alice", "K", "+15550101");
    // Expected: IllegalArgumentException thrown
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
    // "Email already registered: alice@example.com"
}
```

### Scenario 3: Invalid Email Format
```java
try {
    system.registerUser("not-an-email", "Pass123", "Bob", "Smith", "+15550102");
    // Expected: IllegalArgumentException thrown
} catch (IllegalArgumentException e) {
    // "Invalid Email format"
}
```

### Scenario 4: Weak Password
```java
try {
    system.registerUser("bob@example.com", "123", "Bob", "Smith", "+15550102");
    // Expected: IllegalArgumentException thrown
} catch (IllegalArgumentException e) {
    // "Password must be at least 8 characters"
}
```

### Scenario 5: Successful Login
```java
Optional<User> loggedIn = system.login("alice@example.com", "SecurePass123");

// Expected: loggedIn.isPresent() == true
// loggedIn.get().getEmail().equals("alice@example.com")
```

### Scenario 6: Failed Login - Wrong Password
```java
Optional<User> loggedIn = system.login("alice@example.com", "WrongPassword");

// Expected: loggedIn.isEmpty() == true
```

---

## Event Creation & Management

### Scenario 7: Create Event Successfully
```java
Address address = new Address(
    "100 Arena Blvd",
    "Los Angeles",
    "CA",
    "90001",
    "USA"
);

Venue venue = new Venue(
    "Staples Center",
    address,
    20000,
    "Parking, Restaurants, Premium Lounges"
);

Event event = system.createEvent(
    "NBA Finals Game 7",
    "Championship deciding game",
    LocalDateTime.now().plusMonths(3),
    venue,
    EventCategory.SPORTS,
    18000
);

// Expected: event.getEventId() != null
// event.getStatus() == EventStatus.UPCOMING
// event.isBookable() == true
```

### Scenario 8: Create Event with Past Date
```java
try {
    Event event = system.createEvent(
        "Past Event",
        "This should fail",
        LocalDateTime.now().minusDays(1),  // Past date
        venue,
        EventCategory.CONCERT,
        1000
    );
} catch (IllegalArgumentException e) {
    // "Event date must be in the future"
}
```

### Scenario 9: Add Multiple Ticket Types
```java
// VIP Tickets
system.addTicketTypeToEvent(
    eventId,
    "Courtside VIP",
    "Premium courtside seating with lounge access",
    new BigDecimal("2500.00"),
    50,
    TicketTier.VIP
);

// Premium Tickets
system.addTicketTypeToEvent(
    eventId,
    "Lower Bowl Premium",
    "Lower bowl seating",
    new BigDecimal("500.00"),
    500,
    TicketTier.PREMIUM
);

// Standard Tickets
system.addTicketTypeToEvent(
    eventId,
    "Upper Deck",
    "Upper deck seating",
    new BigDecimal("150.00"),
    1000,
    TicketTier.STANDARD
);

Event updatedEvent = system.getEventDetails(eventId);
// Expected: updatedEvent.getTicketTypes().size() == 3
```

### Scenario 10: Search Events
```java
// Create multiple events
Event concert = system.createEvent("Rock Concert", ..., EventCategory.CONCERT, ...);
Event game = system.createEvent("Basketball Game", ..., EventCategory.SPORTS, ...);
Event theater = system.createEvent("Broadway Show", ..., EventCategory.THEATER, ...);

// Search by name
List<Event> rockEvents = system.searchEvents("Rock");
// Expected: rockEvents.contains(concert) == true
// rockEvents.size() >= 1

// Get by category
List<Event> sportsEvents = system.getEventsByCategory(EventCategory.SPORTS);
// Expected: sportsEvents.contains(game) == true
```

---

## Booking Workflow

### Scenario 11: Simple Booking - Single Ticket Type
```java
Map<String, Integer> ticketRequests = new HashMap<>();
ticketRequests.put(standardTicketId, 3);

Booking booking = system.bookTickets(userId, eventId, ticketRequests);

// Expected: booking.getTotalTickets() == 3
// booking.getStatus() == BookingStatus.PENDING
// booking.getTotalAmount() == standardTicketPrice * 3
```

### Scenario 12: Complex Booking - Multiple Ticket Types
```java
Map<String, Integer> ticketRequests = new HashMap<>();
ticketRequests.put(vipTicketId, 2);
ticketRequests.put(premiumTicketId, 4);
ticketRequests.put(standardTicketId, 6);

Booking booking = system.bookTickets(userId, eventId, ticketRequests);

// Expected: booking.getTotalTickets() == 12
// Total amount = (2 * 2500) + (4 * 500) + (6 * 150) = 7900
```

### Scenario 13: Booking - Insufficient Tickets
```java
// Assume only 2 VIP tickets available
Map<String, Integer> ticketRequests = new HashMap<>();
ticketRequests.put(vipTicketId, 10);  // Request more than available

try {
    Booking booking = system.bookTickets(userId, eventId, ticketRequests);
} catch (IllegalStateException e) {
    // "Insufficient tickets available for Courtside VIP"
}
```

### Scenario 14: Booking for Cancelled Event
```java
system.cancelEvent(eventId);

Map<String, Integer> ticketRequests = new HashMap<>();
ticketRequests.put(ticketTypeId, 2);

try {
    Booking booking = system.bookTickets(userId, eventId, ticketRequests);
} catch (IllegalStateException e) {
    // "Event is not available for booking"
}
```

---

## Payment Processing

### Scenario 15: Successful Payment
```java
Booking booking = system.bookTickets(userId, eventId, ticketRequests);

system.makePayment(booking.getBookingId(), PaymentMethod.CREDIT_CARD);

Booking confirmedBooking = system.getBookingDetails(booking.getBookingId());

// Expected: confirmedBooking.getStatus() == BookingStatus.CONFIRMED
// confirmedBooking.getPayment() != null
// confirmedBooking.getPayment().getStatus() == PaymentStatus.COMPLETED
// confirmedBooking.getPayment().getTransactionReference() != null
```

### Scenario 16: Payment with Different Methods
```java
// Credit Card
system.makePayment(booking1.getBookingId(), PaymentMethod.CREDIT_CARD);

// PayPal
system.makePayment(booking2.getBookingId(), PaymentMethod.PAYPAL);

// UPI
system.makePayment(booking3.getBookingId(), PaymentMethod.UPI);

// All should succeed (90% success rate in simulation)
```

### Scenario 17: Failed Payment (Simulated)
```java
Booking booking = system.bookTickets(userId, eventId, ticketRequests);

try {
    system.makePayment(booking.getBookingId(), PaymentMethod.CREDIT_CARD);
    // May fail due to simulated 10% failure rate
} catch (IllegalStateException e) {
    Booking failedBooking = system.getBookingDetails(booking.getBookingId());
    // failedBooking.getStatus() == BookingStatus.FAILED
    // Tickets should be released back
}
```

---

## Cancellation & Refunds

### Scenario 18: Cancel Booking (More than 24h before event)
```java
// Event is 3 months away
Booking booking = system.bookTickets(userId, eventId, ticketRequests);
system.makePayment(booking.getBookingId(), PaymentMethod.CREDIT_CARD);

// Cancel booking
system.cancelBooking(booking.getBookingId());

Booking cancelledBooking = system.getBookingDetails(booking.getBookingId());

// Expected: cancelledBooking.getStatus() == BookingStatus.REFUNDED
// cancelledBooking.getPayment().getStatus() == PaymentStatus.REFUNDED
// All tickets released back to inventory
```

### Scenario 19: Cannot Cancel (Less than 24h before event)
```java
// Create event 12 hours from now
Event soonEvent = system.createEvent(
    "Soon Event",
    "Event in 12 hours",
    LocalDateTime.now().plusHours(12),
    venue,
    EventCategory.CONCERT,
    1000
);

Booking booking = system.bookTickets(userId, soonEvent.getEventId(), ticketRequests);
system.makePayment(booking.getBookingId(), PaymentMethod.CREDIT_CARD);

try {
    system.cancelBooking(booking.getBookingId());
} catch (IllegalStateException e) {
    // "Booking cannot be cancelled (must be at least 24h before event)"
}
```

---

## Concurrent Booking Scenarios

### Scenario 20: Multiple Users Booking Last Tickets
```java
// Simulate concurrent booking for last 3 tickets
TicketType limitedTicket = ...;  // Only 3 tickets available

// User 1 requests 2 tickets
Thread user1Thread = new Thread(() -> {
    Map<String, Integer> req = new HashMap<>();
    req.put(limitedTicket.getTicketTypeId(), 2);
    system.bookTickets(user1Id, eventId, req);
});

// User 2 requests 2 tickets (should partially fail)
Thread user2Thread = new Thread(() -> {
    Map<String, Integer> req = new HashMap<>();
    req.put(limitedTicket.getTicketTypeId(), 2);
    try {
        system.bookTickets(user2Id, eventId, req);
    } catch (IllegalStateException e) {
        // Only 1 ticket left, cannot book 2
    }
});

user1Thread.start();
user2Thread.start();
user1Thread.join();
user2Thread.join();

// Expected: One succeeds with 2 tickets, other fails
// Total booked should not exceed 3
```

---

## Reporting & Analytics Scenarios

### Scenario 21: View User Booking History
```java
// User makes multiple bookings
Booking booking1 = system.bookTickets(userId, event1Id, tickets1);
Booking booking2 = system.bookTickets(userId, event2Id, tickets2);
Booking booking3 = system.bookTickets(userId, event3Id, tickets3);

List<Booking> history = system.getUserBookingHistory(userId);

// Expected: history.size() == 3
// Contains all three bookings
```

### Scenario 22: Get Upcoming Bookings
```java
// User has past, upcoming, and cancelled bookings
List<Booking> upcoming = system.getUpcomingBookings(userId);

// Expected: Only returns CONFIRMED bookings for future events
// Does not include CANCELLED, REFUNDED, or FAILED bookings
```

### Scenario 23: Event Booking Report
```java
List<Booking> eventBookings = system.getEventBookings(eventId);

// Calculate statistics
int totalBookings = eventBookings.size();
int confirmedBookings = eventBookings.stream()
    .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
    .count();
int totalTicketsSold = eventBookings.stream()
    .mapToInt(Booking::getTotalTickets)
    .sum();
BigDecimal totalRevenue = eventBookings.stream()
    .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
    .map(Booking::getTotalAmount)
    .reduce(BigDecimal.ZERO, BigDecimal::add);
```

### Scenario 24: Check Event Capacity
```java
Event event = system.getEventDetails(eventId);
int totalCapacity = event.getTotalCapacity();
int availableCapacity = system.checkEventAvailability(eventId);
int soldTickets = totalCapacity - availableCapacity;

double occupancyRate = (double) soldTickets / totalCapacity * 100;

System.out.printf("Event: %s%n", event.getName());
System.out.printf("Total Capacity: %d%n", totalCapacity);
System.out.printf("Sold: %d (%.1f%%)%n", soldTickets, occupancyRate);
System.out.printf("Available: %d%n", availableCapacity);
```

---

## Edge Cases & Error Handling

### Scenario 25: Book with Non-existent Event ID
```java
try {
    system.bookTickets(userId, "non-existent-event-id", ticketRequests);
} catch (EventNotFoundException e) {
    // Expected: "Event not found: non-existent-event-id"
}
```

### Scenario 26: Book with Non-existent User ID
```java
try {
    system.bookTickets("non-existent-user-id", eventId, ticketRequests);
} catch (IllegalArgumentException e) {
    // Expected: "User not found: non-existent-user-id"
}
```

### Scenario 27: Book with Invalid Ticket Type
```java
Map<String, Integer> ticketRequests = new HashMap<>();
ticketRequests.put("invalid-ticket-type-id", 2);

try {
    system.bookTickets(userId, eventId, ticketRequests);
} catch (IllegalArgumentException e) {
    // Expected: "Ticket type not found"
}
```

### Scenario 28: Book Zero Tickets
```java
Map<String, Integer> ticketRequests = new HashMap<>();
ticketRequests.put(ticketTypeId, 0);

Booking booking = system.bookTickets(userId, eventId, ticketRequests);
// Expected: booking.getTotalTickets() == 0
// Edge case - technically valid but unusual
```

---

## Complex Integration Scenarios

### Scenario 29: Full Booking Lifecycle
```java
// 1. User Registration
User user = system.registerUser(...);

// 2. Event Creation
Event event = system.createEvent(...);
system.addTicketTypeToEvent(event.getEventId(), ...);

// 3. Browse Events
List<Event> events = system.browseUpcomingEvents();

// 4. Search Specific Event
Event foundEvent = system.getEventDetails(event.getEventId());

// 5. Make Booking
Map<String, Integer> tickets = new HashMap<>();
tickets.put(ticketTypeId, 3);
Booking booking = system.bookTickets(user.getUserId(), event.getEventId(), tickets);

// 6. Process Payment
system.makePayment(booking.getBookingId(), PaymentMethod.CREDIT_CARD);

// 7. View Confirmation
Booking confirmed = system.getBookingDetails(booking.getBookingId());
System.out.println("Booking confirmed: " + confirmed.getBookingId());
System.out.println("Transaction: " + confirmed.getPayment().getTransactionReference());

// 8. View Tickets
for (Ticket ticket : confirmed.getTickets()) {
    System.out.println("Ticket: " + ticket.getTicketId());
    System.out.println("Seat: " + ticket.getSeatNumber());
}

// 9. View Booking History
List<Booking> history = system.getUserBookingHistory(user.getUserId());
```

### Scenario 30: Multi-Event Booking
```java
User user = system.registerUser(...);

Event concert = system.createEvent("Concert", ..., EventCategory.CONCERT, ...);
Event game = system.createEvent("Game", ..., EventCategory.SPORTS, ...);
Event show = system.createEvent("Show", ..., EventCategory.THEATER, ...);

// User books tickets for all events
Booking booking1 = system.bookTickets(user.getUserId(), concert.getEventId(), ...);
Booking booking2 = system.bookTickets(user.getUserId(), game.getEventId(), ...);
Booking booking3 = system.bookTickets(user.getUserId(), show.getEventId(), ...);

// Process all payments
system.makePayment(booking1.getBookingId(), PaymentMethod.CREDIT_CARD);
system.makePayment(booking2.getBookingId(), PaymentMethod.PAYPAL);
system.makePayment(booking3.getBookingId(), PaymentMethod.UPI);

// View complete history
List<Booking> allBookings = system.getUserBookingHistory(user.getUserId());
// Expected: 3 confirmed bookings
```

---

## Performance Testing Scenarios

### Scenario 31: High Volume Event Creation
```java
long startTime = System.currentTimeMillis();

for (int i = 0; i < 1000; i++) {
    Event event = system.createEvent(
        "Event " + i,
        "Test event",
        LocalDateTime.now().plusMonths(1).plusDays(i),
        venue,
        EventCategory.CONCERT,
        1000
    );
}

long endTime = System.currentTimeMillis();
System.out.println("Created 1000 events in " + (endTime - startTime) + "ms");
```

### Scenario 32: Bulk Booking Simulation
```java
// Simulate 100 users booking tickets
ExecutorService executor = Executors.newFixedThreadPool(10);

for (int i = 0; i < 100; i++) {
    int userId = i;
    executor.submit(() -> {
        User user = system.registerUser("user" + userId + "@test.com", ...);
        Map<String, Integer> tickets = new HashMap<>();
        tickets.put(ticketTypeId, 2);
        
        try {
            Booking booking = system.bookTickets(user.getUserId(), eventId, tickets);
            system.makePayment(booking.getBookingId(), PaymentMethod.CREDIT_CARD);
        } catch (Exception e) {
            // Some may fail due to sold out
        }
    });
}

executor.shutdown();
executor.awaitTermination(1, TimeUnit.MINUTES);

// Check final event capacity
int remaining = system.checkEventAvailability(eventId);
System.out.println("Remaining capacity: " + remaining);
```

---

## Summary

These test scenarios cover:
- ✓ User registration and authentication
- ✓ Event creation and management
- ✓ Ticket type configuration
- ✓ Simple and complex bookings
- ✓ Payment processing
- ✓ Cancellation and refunds
- ✓ Concurrent operations
- ✓ Reporting and analytics
- ✓ Error handling
- ✓ Edge cases
- ✓ Integration workflows
- ✓ Performance testing

Each scenario can be implemented as unit tests or integration tests to ensure system reliability and correctness.
