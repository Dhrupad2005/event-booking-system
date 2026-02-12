package com.eventbooking;

import com.eventbooking.facade.BookingSystemFacade;
import com.eventbooking.factory.BookingSystemFactory;
import com.eventbooking.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Main application class demonstrating the Event Ticket Booking System
 */
public class BookingSystemApplication {
    
    public static void main(String[] args) {
        System.out.println("=== Event Ticket Booking System ===\n");
        
        // Initialize the system
        BookingSystemFacade bookingSystem = BookingSystemFactory.createBookingSystem();
        
        try {
            // Demo 1: User Registration
            System.out.println("--- Demo 1: User Registration ---");
            User customer = bookingSystem.registerUser(
                "john.doe@email.com",
                "password123",
                "John",
                "Doe",
                "+1234567890"
            );
            System.out.println("User registered: " + customer);
            System.out.println();
            
            // Demo 2: User Login
            System.out.println("--- Demo 2: User Login ---");
            Optional<User> loggedInUser = bookingSystem.login("john.doe@email.com", "password123");
            if (loggedInUser.isPresent()) {
                System.out.println("Login successful: " + loggedInUser.get().getFullName());
            }
            System.out.println();
            
            // Demo 3: Create Event
            System.out.println("--- Demo 3: Create Event ---");
            Address venueAddress = new Address(
                "123 Concert Street",
                "New York",
                "NY",
                "10001",
                "USA"
            );
            Venue venue = new Venue("Madison Square Garden", venueAddress, 20000, "Parking, Food Court");
            
            Event event = bookingSystem.createEvent(
                "Rock Concert 2026",
                "Amazing rock concert featuring top artists",
                LocalDateTime.now().plusMonths(2),
                venue,
                EventCategory.CONCERT,
                1000
            );
            System.out.println("Event created: " + event);
            System.out.println();
            
            // Demo 4: Add Ticket Types
            System.out.println("--- Demo 4: Add Ticket Types ---");
            bookingSystem.addTicketTypeToEvent(
                event.getEventId(),
                "VIP Pass",
                "Access to VIP lounge and front row seats",
                new BigDecimal("250.00"),
                100,
                TicketTier.VIP
            );
            
            bookingSystem.addTicketTypeToEvent(
                event.getEventId(),
                "General Admission",
                "Standard entry ticket",
                new BigDecimal("75.00"),
                500,
                TicketTier.STANDARD
            );
            
            bookingSystem.addTicketTypeToEvent(
                event.getEventId(),
                "Early Bird Special",
                "Discounted early booking",
                new BigDecimal("60.00"),
                200,
                TicketTier.EARLY_BIRD
            );
            
            Event updatedEvent = bookingSystem.getEventDetails(event.getEventId());
            System.out.println("Ticket types added. Event now has " + 
                             updatedEvent.getTicketTypes().size() + " ticket types");
            for (TicketType tt : updatedEvent.getTicketTypes()) {
                System.out.println("  - " + tt);
            }
            System.out.println();
            
            // Demo 5: Browse Events
            System.out.println("--- Demo 5: Browse Upcoming Events ---");
            List<Event> upcomingEvents = bookingSystem.browseUpcomingEvents();
            System.out.println("Found " + upcomingEvents.size() + " upcoming events:");
            upcomingEvents.forEach(e -> System.out.println("  - " + e.getName()));
            System.out.println();
            
            // Demo 6: Book Tickets
            System.out.println("--- Demo 6: Book Tickets ---");
            Map<String, Integer> ticketRequests = new HashMap<>();
            TicketType vipTicket = updatedEvent.getTicketTypes().get(0);
            TicketType generalTicket = updatedEvent.getTicketTypes().get(1);
            
            ticketRequests.put(vipTicket.getTicketTypeId(), 2);
            ticketRequests.put(generalTicket.getTicketTypeId(), 3);
            
            Booking booking = bookingSystem.bookTickets(
                customer.getUserId(),
                event.getEventId(),
                ticketRequests
            );
            System.out.println("Booking created: " + booking);
            System.out.println("Total tickets: " + booking.getTotalTickets());
            System.out.println("Total amount: $" + booking.getTotalAmount());
            System.out.println();
            
            // Demo 7: Process Payment
            System.out.println("--- Demo 7: Process Payment ---");
            bookingSystem.makePayment(booking.getBookingId(), PaymentMethod.CREDIT_CARD);
            
            Booking confirmedBooking = bookingSystem.getBookingDetails(booking.getBookingId());
            System.out.println("Booking status: " + confirmedBooking.getStatus());
            System.out.println("Payment status: " + confirmedBooking.getPayment().getStatus());
            System.out.println("Transaction reference: " + 
                             confirmedBooking.getPayment().getTransactionReference());
            System.out.println();
            
            // Demo 8: View Booking History
            System.out.println("--- Demo 8: View Booking History ---");
            List<Booking> userBookings = bookingSystem.getUserBookingHistory(customer.getUserId());
            System.out.println(customer.getFullName() + " has " + userBookings.size() + " booking(s):");
            for (Booking b : userBookings) {
                System.out.println("  - " + b.getBookingId() + " | " + 
                                 b.getEvent().getName() + " | Status: " + b.getStatus());
            }
            System.out.println();
            
            // Demo 9: Check Event Availability
            System.out.println("--- Demo 9: Check Event Availability ---");
            int availableCapacity = bookingSystem.checkEventAvailability(event.getEventId());
            System.out.println("Event: " + event.getName());
            System.out.println("Available capacity: " + availableCapacity + "/" + event.getTotalCapacity());
            System.out.println();
            
            // Demo 10: Search Events
            System.out.println("--- Demo 10: Search Events ---");
            List<Event> searchResults = bookingSystem.searchEvents("Rock");
            System.out.println("Search results for 'Rock': " + searchResults.size() + " event(s) found");
            searchResults.forEach(e -> System.out.println("  - " + e.getName()));
            System.out.println();
            
            // Demo 11: View Ticket Details
            System.out.println("--- Demo 11: View Ticket Details ---");
            List<Ticket> tickets = confirmedBooking.getTickets();
            System.out.println("Tickets for booking " + confirmedBooking.getBookingId() + ":");
            for (Ticket ticket : tickets) {
                System.out.println("  - Ticket ID: " + ticket.getTicketId());
                System.out.println("    Type: " + ticket.getTicketType().getName());
                System.out.println("    Seat: " + ticket.getSeatNumber());
                System.out.println("    Price: $" + ticket.getPricePaid());
                System.out.println("    Status: " + ticket.getStatus());
                System.out.println("    Valid: " + ticket.isValid());
                System.out.println();
            }
            
            System.out.println("=== Demo Completed Successfully ===");
            
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
