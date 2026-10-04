package com.ecommerce.orderservice.dto;

import java.math.BigDecimal;

public record OrderItemDto(
    String productId,
    String productName,
    BigDecimal unitPrice,
    int quantity
) {}
