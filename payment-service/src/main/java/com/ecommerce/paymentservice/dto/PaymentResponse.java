package com.ecommerce.paymentservice.dto;

import com.ecommerce.paymentservice.entity.Payment;
import java.math.BigDecimal;

public record PaymentResponse(
        String id, String orderId, BigDecimal amount,
        String status, String method, String transactionId
) {
    public static PaymentResponse from(Payment p) {
        return new PaymentResponse(
                p.getId(), p.getOrderId(), p.getAmount(),
                p.getStatus().name(), p.getMethod().name(), p.getTransactionId()
        );
    }
}
