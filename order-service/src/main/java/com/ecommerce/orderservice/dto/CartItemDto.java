package com.ecommerce.orderservice.dto;

import java.math.BigDecimal;

public record CartItemDto(
    String id,
    String productId,
    String productName,
    BigDecimal unitPrice,
    int quantity
) {}
