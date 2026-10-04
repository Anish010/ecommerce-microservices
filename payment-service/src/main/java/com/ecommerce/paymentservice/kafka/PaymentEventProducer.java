package com.ecommerce.paymentservice.kafka;

import com.ecommerce.paymentservice.event.PaymentStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer {

    private static final String TOPIC = "payment.status";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentStatus(PaymentStatusEvent event) {
        kafkaTemplate.send(TOPIC, event.orderId(), event);
        log.info("Published payment status event for order: {} -> {}", event.orderId(), event.status());
    }
}