package com.ecommerce.orderservice.dto;

import java.math.BigDecimal;

// Response DTO from product-service (via Feign)
public record ProductDto(
    String id,
    String name,
    BigDecimal price
) {}
