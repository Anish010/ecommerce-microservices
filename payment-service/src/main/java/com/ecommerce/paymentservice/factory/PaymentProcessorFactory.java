package com.ecommerce.paymentservice.factory;

import com.ecommerce.paymentservice.entity.PaymentMethod;
import com.ecommerce.paymentservice.strategy.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

// Factory Pattern: selects the right strategy at runtime based on PaymentMethod.
// Adding a new payment provider = implement PaymentStrategy + annotate @Component.
// Zero changes needed here (Open/Closed Principle).
@Component
@RequiredArgsConstructor
public class PaymentProcessorFactory {

    private final List<PaymentStrategy> strategies; // Spring injects all implementations

    public PaymentStrategy getStrategy(PaymentMethod method) {
        return strategies.stream()
                .filter(s -> s.supports(method))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No payment strategy found for method: " + method));
    }
}
