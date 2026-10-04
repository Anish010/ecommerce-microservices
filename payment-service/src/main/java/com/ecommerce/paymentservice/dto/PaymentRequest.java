package com.ecommerce.paymentservice.dto;

import com.ecommerce.paymentservice.entity.PaymentMethod;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record PaymentRequest(
        @NotBlank String orderId,
        @NotNull @Positive BigDecimal amount,
        @NotNull PaymentMethod method
) {}
