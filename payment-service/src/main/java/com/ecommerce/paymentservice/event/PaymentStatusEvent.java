package com.ecommerce.paymentservice.event;

import java.time.LocalDateTime;

// Event published to the "payment.status" Kafka topic, consumed by order-service
public record PaymentStatusEvent(
        String orderId,
        String paymentId,
        String status,
        String transactionId,
        LocalDateTime updatedAt
) {}
