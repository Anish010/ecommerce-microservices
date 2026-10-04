package com.ecommerce.paymentservice.strategy;

import com.ecommerce.paymentservice.entity.PaymentMethod;
import java.math.BigDecimal;

// Strategy Pattern: defines the contract for any payment processor
public interface PaymentStrategy {
    PaymentResult process(String orderId, BigDecimal amount, PaymentDetails details);
    boolean supports(PaymentMethod method);
}
