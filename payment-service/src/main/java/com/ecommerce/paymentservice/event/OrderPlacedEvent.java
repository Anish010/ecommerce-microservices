package com.ecommerce.paymentservice.event;

import java.math.BigDecimal;

// Event consumed from the "order.placed" Kafka topic, published by order-service
public record OrderPlacedEvent(
        String orderId,
        String customerId,
        BigDecimal totalAmount
) {}
