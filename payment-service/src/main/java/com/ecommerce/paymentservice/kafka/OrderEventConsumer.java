package com.ecommerce.paymentservice.kafka;

import com.ecommerce.paymentservice.dto.PaymentRequest;
import com.ecommerce.paymentservice.entity.PaymentMethod;
import com.ecommerce.paymentservice.event.OrderPlacedEvent;
import com.ecommerce.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics = "order.placed", groupId = "payment-service-group")
    public void handleOrderPlaced(OrderPlacedEvent event) {
        log.info("Processing payment for order: {}", event.orderId());
        paymentService.initiatePayment(
                new PaymentRequest(event.orderId(), event.totalAmount(), PaymentMethod.SIMULATED)
        );
    }
}
