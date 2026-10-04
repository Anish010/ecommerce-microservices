package com.ecommerce.orderservice.dto;

import jakarta.validation.constraints.NotBlank;

public record PlaceOrderRequest(
    @NotBlank String deliveryStreet,
    @NotBlank String deliveryCity,
    @NotBlank String deliveryState,
    @NotBlank String deliveryZipCode,
    @NotBlank String deliveryCountry
) {}
