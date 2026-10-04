package com.ecommerce.orderservice.dto;

import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
    String id,
    String userId,
    List<OrderItemDto> items,
    OrderStatus status,
    BigDecimal totalAmount,
    String deliveryStreet,
    String deliveryCity,
    String deliveryState,
    String deliveryZipCode,
    String deliveryCountry,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getUserId(),
            order.getItems().stream()
                .map(i -> new OrderItemDto(i.getProductId(), i.getProductName(), i.getUnitPrice(), i.getQuantity()))
                .toList(),
            order.getStatus(),
            order.getTotalAmount(),
            order.getDeliveryStreet(),
            order.getDeliveryCity(),
            order.getDeliveryState(),
            order.getDeliveryZipCode(),
            order.getDeliveryCountry(),
            order.getCreatedAt(),
            order.getUpdatedAt()
        );
    }
}
