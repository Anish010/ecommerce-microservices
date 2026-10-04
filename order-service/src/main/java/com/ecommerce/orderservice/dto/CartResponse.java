package com.ecommerce.orderservice.dto;

import com.ecommerce.orderservice.entity.Cart;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
    String id,
    String userId,
    List<CartItemDto> items,
    BigDecimal totalAmount,
    int totalItems
) {
    public static CartResponse from(Cart cart) {
        return new CartResponse(
            cart.getId(),
            cart.getUserId(),
            cart.getItems().stream()
                .map(i -> new CartItemDto(i.getId(), i.getProductId(), i.getProductName(), i.getUnitPrice(), i.getQuantity()))
                .toList(),
            cart.getTotalAmount(),
            cart.getTotalItems()
        );
    }
}
