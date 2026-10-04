package com.ecommerce.paymentservice.strategy;

import com.ecommerce.paymentservice.entity.PaymentMethod;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.UUID;

// SimulatedPaymentStrategy: always returns success for demo purposes.
// Replace with StripePaymentStrategy or RazorpayPaymentStrategy for production.
@Component
@Order(1)
public class SimulatedPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResult process(String orderId, BigDecimal amount, PaymentDetails details) {
        // Simulate a 200ms payment processing delay
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        var transactionId = "SIM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return PaymentResult.success(transactionId);
    }

    @Override
    public boolean supports(PaymentMethod method) {
        return method == PaymentMethod.SIMULATED;
    }
}
