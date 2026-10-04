package com.ecommerce.orderservice.event;

import com.ecommerce.orderservice.dto.OrderItemDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// Kafka event published when order is placed
public record OrderPlacedEvent(
    String orderId,
    String userId,
    BigDecimal totalAmount,
    List<OrderItemDto> items,
    LocalDateTime timestamp
) {}
