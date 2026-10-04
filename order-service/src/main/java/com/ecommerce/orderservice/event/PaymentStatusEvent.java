package com.ecommerce.orderservice.event;

import java.time.LocalDateTime;

// Kafka event consumed from payment-service
public record PaymentStatusEvent(
    String orderId,
    String paymentId,
    String status,      // SUCCESS or FAILED
    String transactionId,
    LocalDateTime timestamp
) {}
