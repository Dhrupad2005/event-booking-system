package com.eventbooking.factory;

import com.eventbooking.facade.BookingSystemFacade;
import com.eventbooking.repository.BookingRepository;
import com.eventbooking.repository.EventRepository;
import com.eventbooking.repository.UserRepository;
import com.eventbooking.repository.impl.BookingRepositoryImpl;
import com.eventbooking.repository.impl.EventRepositoryImpl;
import com.eventbooking.repository.impl.UserRepositoryImpl;
import com.eventbooking.service.BookingService;
import com.eventbooking.service.EventService;
import com.eventbooking.service.PaymentService;
import com.eventbooking.service.UserService;

/**
 * Factory class for creating and wiring system components
 * Implements Dependency Injection pattern
 */
public class BookingSystemFactory {
    
    /**
     * Create a fully configured BookingSystemFacade
     */
    public static BookingSystemFacade createBookingSystem() {
        // Create repositories
        UserRepository userRepository = new UserRepositoryImpl();
        EventRepository eventRepository = new EventRepositoryImpl();
        BookingRepository bookingRepository = new BookingRepositoryImpl();
        
        // Create services
        UserService userService = new UserService(userRepository);
        EventService eventService = new EventService(eventRepository);
        PaymentService paymentService = new PaymentService();
        BookingService bookingService = new BookingService(
            bookingRepository, eventService, userService, paymentService);
        
        // Create facade
        return new BookingSystemFacade(userService, eventService, bookingService);
    }
}
