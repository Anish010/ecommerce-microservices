package com.ecommerce.userservice.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
        @NotBlank String street,
        String city,
        String state,
        String zipCode,
        @NotBlank String country,
        boolean isDefault
) {}