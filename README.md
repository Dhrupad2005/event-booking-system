# Event Ticket Booking System

A comprehensive, industry-level Event Ticket Booking System built with Java using Object-Oriented Programming principles.

## 🏗️ Architecture Overview

This system follows a **layered architecture** with clear separation of concerns:

```
┌─────────────────────────────────────────────┐
│         Application Layer (Main)            │
│        BookingSystemApplication.java        │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│          Facade Layer (API)                 │
│        BookingSystemFacade.java             │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│          Service Layer                      │
│  EventService, UserService,                 │
│  BookingService, PaymentService             │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│       Repository Layer (Data Access)        │
│  EventRepository, UserRepository,           │
│  BookingRepository                          │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│          Model Layer (Domain)               │
│  Event, User, Booking, Ticket,              │
│  Payment, Venue, Address                    │
└─────────────────────────────────────────────┘
```

## 🎯 Key Features

### User Management
- User registration and authentication
- Profile management
- Role-based access control (Admin, Event Organizer, Customer)

### Event Management
- Create and manage events
- Multiple ticket types per event
- Venue and location tracking
- Event categories and status tracking

### Booking System
- Multi-ticket booking in single transaction
- Real-time seat/ticket reservation
- Automatic ticket number generation
- Booking history tracking

### Payment Processing
- Multiple payment methods
- Payment status tracking
- Automatic refund processing
- Transaction reference generation

### Advanced Features
- Thread-safe ticket reservation
- Event capacity management
- Cancellation with 24-hour policy
- Search and filter functionality

## 📦 Project Structure

```
event-booking-system/
├── src/main/java/com/eventbooking/
│   ├── model/                    # Domain models
│   │   ├── Event.java
│   │   ├── User.java
│   │   ├── Booking.java
│   │   ├── Ticket.java
│   │   ├── Payment.java
│   │   ├── Venue.java
│   │   ├── Address.java
│   │   ├── TicketType.java
│   │   └── enums/               # Enumerations
│   │       ├── EventStatus.java
│   │       ├── EventCategory.java
│   │       ├── BookingStatus.java
│   │       ├── PaymentStatus.java
│   │       └── UserRole.java
│   │
│   ├── repository/              # Data access layer
│   │   ├── Repository.java
│   │   ├── EventRepository.java
│   │   ├── UserRepository.java
│   │   ├── BookingRepository.java
│   │   └── impl/
│   │       ├── EventRepositoryImpl.java
│   │       ├── UserRepositoryImpl.java
│   │       └── BookingRepositoryImpl.java
│   │
│   ├── service/                 # Business logic layer
│   │   ├── EventService.java
│   │   ├── UserService.java
│   │   ├── BookingService.java
│   │   └── PaymentService.java
│   │
│   ├── facade/                  # Simplified API
│   │   └── BookingSystemFacade.java
│   │
│   ├── factory/                 # Object creation
│   │   └── BookingSystemFactory.java
│   │
│   ├── exception/               # Custom exceptions
│   │   ├── BookingSystemException.java
│   │   └── Exceptions.java
│   │
│   └── BookingSystemApplication.java  # Main entry point
```

## 🔑 Design Patterns Used

### 1. **Repository Pattern**
- Abstracts data access logic
- Interface-based design for flexibility
- In-memory implementation (easily replaceable with database)

### 2. **Facade Pattern**
- `BookingSystemFacade` provides simplified interface
- Hides complexity of multiple services
- Single entry point for client code

### 3. **Factory Pattern**
- `BookingSystemFactory` handles object creation
- Centralizes dependency injection
- Manages object lifecycle

### 4. **Value Object Pattern**
- `Address` is immutable
- Encapsulates related data
- Provides value equality

### 5. **Service Layer Pattern**
- Business logic separated from data access
- Transaction management
- Validation and error handling

## 🎨 OOP Principles Applied

### 1. **Encapsulation**
- Private fields with public getters/setters
- Data hiding and controlled access
- Business logic encapsulated in methods

### 2. **Inheritance**
- Custom exceptions extend base exception
- Common repository interface

### 3. **Polymorphism**
- Repository implementations
- Payment method variations
- Event category handling

### 4. **Abstraction**
- Repository interfaces
- Service layer abstractions
- Clear contracts between layers

## 🔒 SOLID Principles

### Single Responsibility Principle (SRP)
- Each class has one reason to change
- `EventService` handles only event operations
- `PaymentService` handles only payments

### Open/Closed Principle (OCP)
- Open for extension via interfaces
- Closed for modification via abstraction
- New payment methods can be added without changing core code

### Liskov Substitution Principle (LSP)
- Repository implementations are interchangeable
- Service dependencies use interfaces

### Interface Segregation Principle (ISP)
- Specific repository interfaces
- Clients depend only on methods they use

### Dependency Inversion Principle (DIP)
- High-level modules depend on abstractions
- Services depend on repository interfaces
- Factory handles concrete implementations

## 🚀 How to Run

### Prerequisites
- Java 11 or higher
- No external dependencies (pure Java)

### Compilation
```bash
# Navigate to the project directory
cd event-booking-system

# Compile all Java files
javac -d bin src/main/java/com/eventbooking/**/*.java

# Run the application
java -cp bin com.eventbooking.BookingSystemApplication
```

### Using an IDE
1. Import the project into your IDE (IntelliJ IDEA, Eclipse, etc.)
2. Ensure Java 11+ is configured
3. Run `BookingSystemApplication.java`

## 📝 Usage Examples

### Register a User
```java
BookingSystemFacade system = BookingSystemFactory.createBookingSystem();

User user = system.registerUser(
    "user@email.com",
    "password123",
    "John",
    "Doe",
    "+1234567890"
);
```

### Create an Event
```java
Address address = new Address("123 Street", "City", "State", "12345", "Country");
Venue venue = new Venue("Stadium Name", address, 50000, "Parking, Food");

Event event = system.createEvent(
    "Concert 2026",
    "Amazing concert",
    LocalDateTime.now().plusMonths(2),
    venue,
    EventCategory.CONCERT,
    1000
);
```

### Add Ticket Types
```java
system.addTicketTypeToEvent(
    eventId,
    "VIP Pass",
    "Premium seating",
    new BigDecimal("150.00"),
    100,
    TicketTier.VIP
);
```

### Book Tickets
```java
Map<String, Integer> ticketRequests = new HashMap<>();
ticketRequests.put(ticketTypeId, 2);

Booking booking = system.bookTickets(userId, eventId, ticketRequests);
system.makePayment(booking.getBookingId(), PaymentMethod.CREDIT_CARD);
```

## 🔐 Thread Safety

- Repository implementations use `ConcurrentHashMap`
- Ticket reservation methods are `synchronized`
- Atomic operations for critical sections

## 🎯 Key Classes and Their Responsibilities

| Class | Responsibility |
|-------|---------------|
| `Event` | Represents an event with tickets, venue, and metadata |
| `User` | Represents system users with roles and authentication |
| `Booking` | Aggregates tickets in a single transaction |
| `Ticket` | Individual ticket with seat, price, and status |
| `Payment` | Handles payment transactions and refunds |
| `EventService` | Business logic for event management |
| `BookingService` | Core booking workflow and validation |
| `PaymentService` | Payment processing and refunds |

## 🧪 Testing the System

The `BookingSystemApplication` includes a comprehensive demo that:
1. Registers a user
2. Creates an event
3. Adds multiple ticket types
4. Books tickets
5. Processes payment
6. Views booking history
7. Searches for events
8. Displays ticket details

## 🔄 Future Enhancements

### Database Integration
Replace in-memory repositories with:
- JPA/Hibernate implementations
- PostgreSQL/MySQL database

### Security
- Password hashing (BCrypt)
- JWT authentication
- Role-based authorization

### Additional Features
- Email notifications
- QR code generation for tickets
- Waiting list functionality
- Dynamic pricing
- Reviews and ratings
- Event recommendations

### API Layer
- REST API with Spring Boot
- GraphQL support
- API documentation with Swagger

## 📚 Learning Outcomes

This project demonstrates:
- Clean code architecture
- Industry-standard design patterns
- SOLID principles in practice
- Proper exception handling
- Thread-safe operations
- Separation of concerns
- Domain-driven design

## 👥 Contributing

This is a learning project demonstrating OOP principles. Feel free to:
- Add new features
- Implement database integration
- Add unit tests
- Improve documentation

## 📄 License

Educational project - free to use and modify.

## 🤝 Acknowledgments

Built following industry best practices and design patterns commonly used in enterprise Java applications.
