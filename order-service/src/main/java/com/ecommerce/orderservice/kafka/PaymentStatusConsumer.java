package com.ecommerce.orderservice.kafka;

import com.ecommerce.orderservice.entity.OrderStatus;
import com.ecommerce.orderservice.event.PaymentStatusEvent;
import com.ecommerce.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentStatusConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "payment.status", groupId = "order-service-group")
    public void handlePaymentStatus(PaymentStatusEvent event) {
        log.info("Received payment status for order {}: {}", event.orderId(), event.status());
        var newStatus = switch (event.status()) {
            case "SUCCESS" -> OrderStatus.PAYMENT_SUCCESS;
            case "FAILED"  -> OrderStatus.PAYMENT_FAILED;
            default -> {
                log.warn("Unknown payment status: {}", event.status());
                yield OrderStatus.PAYMENT_FAILED;
            }
        };
        orderService.updateOrderStatus(event.orderId(), newStatus);
    }
}
