package com.ecommerce.paymentservice.strategy;

// Carries payment details (card number etc. — empty for simulated)
public record PaymentDetails(
        String cardNumber,
        String cardHolder,
        String expiryMonth,
        String expiryYear,
        String cvv,
        String upiId
) {}
