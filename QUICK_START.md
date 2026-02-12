# Quick Start Guide

Get up and running with the Event Ticket Booking System in minutes!

## 📋 Prerequisites

- Java 11 or higher installed
- Terminal or Command Prompt access

## 🚀 Quick Start (3 Steps)

### Step 1: Extract and Navigate

```bash
# Navigate to the project directory
cd event-booking-system
```

### Step 2: Build and Run

**For Linux/Mac:**
```bash
chmod +x build.sh
./build.sh
```

**For Windows:**
```cmd
build.bat
```

**Manual Compilation:**
```bash
# Create bin directory
mkdir bin

# Compile all Java files
javac -d bin $(find src/main/java -name "*.java")

# Run the application
java -cp bin com.eventbooking.BookingSystemApplication
```

### Step 3: View the Demo Output

The application will run a comprehensive demo showing:
1. User registration
2. Event creation
3. Ticket type configuration
4. Booking process
5. Payment processing
6. Booking confirmation
7. And more!

## 📚 Project Structure

```
event-booking-system/
├── src/main/java/com/eventbooking/
│   ├── model/              # Domain models (Event, User, Booking, etc.)
│   ├── repository/         # Data access layer
│   ├── service/            # Business logic
│   ├── facade/             # Simplified API
│   ├── factory/            # Object creation
│   ├── exception/          # Custom exceptions
│   └── BookingSystemApplication.java
├── README.md               # Comprehensive documentation
├── OOP_PRINCIPLES.md       # OOP concepts explained
├── UML_DIAGRAMS.md         # Architecture diagrams
├── TEST_SCENARIOS.md       # Test cases
├── build.sh                # Linux/Mac build script
└── build.bat               # Windows build script
```

## 💡 Using the System Programmatically

```java
import com.eventbooking.facade.BookingSystemFacade;
import com.eventbooking.factory.BookingSystemFactory;
import com.eventbooking.model.*;

public class MyApplication {
    public static void main(String[] args) {
        // Initialize the system
        BookingSystemFacade system = BookingSystemFactory.createBookingSystem();
        
        // Register a user
        User user = system.registerUser(
            "user@example.com",
            "password123",
            "John",
            "Doe",
            "+1234567890"
        );
        
        // Create an event
        Address address = new Address("123 Street", "City", "State", "12345", "USA");
        Venue venue = new Venue("Stadium", address, 10000, "Parking available");
        
        Event event = system.createEvent(
            "Concert 2026",
            "Amazing concert",
            LocalDateTime.now().plusMonths(2),
            venue,
            EventCategory.CONCERT,
            1000
        );
        
        // Add ticket types
        system.addTicketTypeToEvent(
            event.getEventId(),
            "VIP Pass",
            "Premium seating",
            new BigDecimal("150.00"),
            100,
            TicketTier.VIP
        );
        
        // Browse events
        List<Event> events = system.browseUpcomingEvents();
        
        // Book tickets
        Map<String, Integer> ticketRequests = new HashMap<>();
        ticketRequests.put(ticketTypeId, 2);
        
        Booking booking = system.bookTickets(
            user.getUserId(),
            event.getEventId(),
            ticketRequests
        );
        
        // Process payment
        system.makePayment(booking.getBookingId(), PaymentMethod.CREDIT_CARD);
        
        // View booking details
        Booking confirmed = system.getBookingDetails(booking.getBookingId());
        System.out.println("Booking ID: " + confirmed.getBookingId());
        System.out.println("Status: " + confirmed.getStatus());
    }
}
```

## 🎯 Key Features

- ✅ User registration and authentication
- ✅ Event management with multiple ticket types
- ✅ Real-time ticket booking with inventory management
- ✅ Payment processing with multiple payment methods
- ✅ Booking cancellation and refunds
- ✅ Thread-safe operations
- ✅ Comprehensive search and filtering
- ✅ Complete booking history tracking

## 📖 Documentation

- **README.md** - Complete system documentation
- **OOP_PRINCIPLES.md** - Deep dive into OOP concepts used
- **UML_DIAGRAMS.md** - System architecture diagrams
- **TEST_SCENARIOS.md** - Comprehensive test scenarios

## 🔧 Customization

### Add New Payment Method

```java
public enum PaymentMethod {
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    PAYPAL("PayPal"),
    UPI("UPI"),
    CRYPTO("Cryptocurrency");  // Add new method here
}
```

### Add New Event Category

```java
public enum EventCategory {
    CONCERT("Concert"),
    SPORTS("Sports"),
    THEATER("Theater"),
    GAMING("Gaming");  // Add new category here
}
```

### Implement Database Storage

Replace in-memory repositories with database implementations:

```java
public class EventRepositoryJpaImpl implements EventRepository {
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Event save(Event event) {
        entityManager.persist(event);
        return event;
    }
    
    // Implement other methods...
}
```

## 🐛 Troubleshooting

### Java Not Found
```bash
# Check Java installation
java -version

# Install Java 11+ if needed
```

### Compilation Errors
```bash
# Ensure you're in the correct directory
pwd

# Clean and rebuild
rm -rf bin
mkdir bin
javac -d bin $(find src/main/java -name "*.java")
```

### Class Not Found Error
```bash
# Ensure classpath is correct
java -cp bin com.eventbooking.BookingSystemApplication
```

## 🎓 Learning Path

1. **Start Here**: Run the demo application
2. **Read**: README.md for architecture overview
3. **Study**: OOP_PRINCIPLES.md for design patterns
4. **Understand**: UML_DIAGRAMS.md for class relationships
5. **Practice**: TEST_SCENARIOS.md for different use cases
6. **Experiment**: Modify code and add new features

## 🤝 Next Steps

- Add unit tests using JUnit
- Implement database persistence with JPA/Hibernate
- Create REST API with Spring Boot
- Add web interface
- Implement email notifications
- Add QR code generation for tickets

## 📞 Support

For questions or issues:
- Review the comprehensive documentation
- Check TEST_SCENARIOS.md for examples
- Study the code comments

---

**Happy Coding! 🚀**
