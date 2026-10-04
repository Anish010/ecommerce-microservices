package com.ecommerce.paymentservice.service.impl;

import com.ecommerce.paymentservice.dto.*;
import com.ecommerce.paymentservice.entity.*;
import com.ecommerce.paymentservice.event.PaymentStatusEvent;
import com.ecommerce.paymentservice.exception.ResourceNotFoundException;
import com.ecommerce.paymentservice.factory.PaymentProcessorFactory;
import com.ecommerce.paymentservice.kafka.PaymentEventProducer;
import com.ecommerce.paymentservice.repository.PaymentRepository;
import com.ecommerce.paymentservice.service.PaymentService;
import com.ecommerce.paymentservice.strategy.PaymentDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentProcessorFactory processorFactory;
    private final PaymentEventProducer eventProducer;

    @Override
    public PaymentResponse initiatePayment(PaymentRequest request) {
        // Check for duplicate payment
        if (paymentRepository.existsByOrderId(request.orderId())) {
            throw new IllegalStateException("Payment already initiated for order: " + request.orderId());
        }

        var payment = Payment.builder()
                .orderId(request.orderId())
                .amount(request.amount())
                .method(request.method())
                .status(PaymentStatus.PROCESSING)
                .build();
        paymentRepository.save(payment);

        // Delegate to appropriate strategy via factory
        var strategy = processorFactory.getStrategy(request.method());
        var details = new PaymentDetails(null, null, null, null, null, null);
        var result = strategy.process(request.orderId(), request.amount(), details);

        // Update payment status
        if (result.success()) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId(result.transactionId());
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason(result.failureReason());
        }
        paymentRepository.save(payment);

        // Publish status back to order-service via Kafka
        eventProducer.publishPaymentStatus(new PaymentStatusEvent(
                request.orderId(),
                payment.getId(),
                payment.getStatus().name(),
                payment.getTransactionId(),
                LocalDateTime.now()
        ));

        log.info("Payment {} for order {}: status={}",
                payment.getId(), request.orderId(), payment.getStatus());

        return PaymentResponse.from(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId)
                .map(PaymentResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order: " + orderId));
    }
}
