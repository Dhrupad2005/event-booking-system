package com.eventbooking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a payment transaction
 */
public class Payment {
    private final String paymentId;
    private final Booking booking;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String transactionReference;
    
    public Payment(Booking booking, BigDecimal amount, PaymentMethod paymentMethod) {
        this.paymentId = UUID.randomUUID().toString();
        this.booking = booking;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = PaymentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }
    
    public void complete(String transactionReference) {
        this.status = PaymentStatus.COMPLETED;
        this.transactionReference = transactionReference;
        this.completedAt = LocalDateTime.now();
    }
    
    public void fail() {
        this.status = PaymentStatus.FAILED;
    }
    
    public void refund() {
        if (status == PaymentStatus.COMPLETED) {
            this.status = PaymentStatus.REFUNDED;
        }
    }
    
    // Getters and Setters
    public String getPaymentId() {
        return paymentId;
    }
    
    public Booking getBooking() {
        return booking;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public PaymentStatus getStatus() {
        return status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public String getTransactionReference() {
        return transactionReference;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(paymentId, payment.paymentId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(paymentId);
    }
    
    @Override
    public String toString() {
        return String.format("Payment{id='%s', amount=%s, method=%s, status=%s}",
                paymentId, amount, paymentMethod, status);
    }
}
