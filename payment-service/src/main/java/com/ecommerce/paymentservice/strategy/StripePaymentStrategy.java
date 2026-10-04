package com.ecommerce.paymentservice.strategy;

import com.ecommerce.paymentservice.entity.PaymentMethod;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

// Stub: wire in Stripe SDK and API key when ready for production
@Component
public class StripePaymentStrategy implements PaymentStrategy {

    // @Value("${stripe.api.key}")
    // private String apiKey;

    @Override
    public PaymentResult process(String orderId, BigDecimal amount, PaymentDetails details) {
        // TODO: Stripe.apiKey = apiKey;
        // TODO: Map<String, Object> params = ... create PaymentIntent ...
        // TODO: PaymentIntent intent = PaymentIntent.create(params);
        // TODO: return PaymentResult.success(intent.getId());
        throw new UnsupportedOperationException("Stripe not configured yet");
    }

    @Override
    public boolean supports(PaymentMethod method) {
        return method == PaymentMethod.CREDIT_CARD || method == PaymentMethod.DEBIT_CARD;
    }
}
