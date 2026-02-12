package com.eventbooking.service;

import com.eventbooking.model.Booking;
import com.eventbooking.model.Payment;
import com.eventbooking.model.PaymentMethod;
import com.eventbooking.model.PaymentStatus;

import java.util.UUID;

/**
 * Service layer for Payment processing
 * In a real system, this would integrate with payment gateways
 */
public class PaymentService {
    
    /**
     * Process payment for a booking
     */
    public Payment processPayment(Booking booking, PaymentMethod paymentMethod) {
        Payment payment = new Payment(booking, booking.getTotalAmount(), paymentMethod);
        
        // Simulate payment processing
        boolean paymentSuccess = simulatePaymentGateway(payment);
        
        if (paymentSuccess) {
            String transactionRef = generateTransactionReference();
            payment.complete(transactionRef);
        } else {
            payment.fail();
        }
        
        return payment;
    }
    
    /**
     * Refund a payment
     */
    public void refundPayment(Payment payment) {
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot refund payment that is not completed");
        }
        
        // Simulate refund processing
        boolean refundSuccess = simulateRefundGateway(payment);
        
        if (refundSuccess) {
            payment.refund();
        } else {
            throw new IllegalStateException("Refund processing failed");
        }
    }
    
    /**
     * Verify payment status
     */
    public PaymentStatus verifyPaymentStatus(Payment payment) {
        // In real implementation, verify with payment gateway
        return payment.getStatus();
    }
    
    // Simulate payment gateway interaction
    private boolean simulatePaymentGateway(Payment payment) {
        // In production, integrate with actual payment gateway
        // For now, simulate success (90% success rate)
        return Math.random() < 0.9;
    }
    
    private boolean simulateRefundGateway(Payment payment) {
        // Simulate refund processing
        return true;
    }
    
    private String generateTransactionReference() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }
}
