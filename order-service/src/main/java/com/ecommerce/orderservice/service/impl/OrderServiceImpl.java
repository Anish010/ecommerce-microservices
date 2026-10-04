package com.ecommerce.orderservice.service.impl;

import com.ecommerce.orderservice.client.ProductClient;
import com.ecommerce.orderservice.dto.*;
import com.ecommerce.orderservice.entity.*;
import com.ecommerce.orderservice.event.OrderPlacedEvent;
import com.ecommerce.orderservice.exception.ResourceNotFoundException;
import com.ecommerce.orderservice.kafka.OrderEventProducer;
import com.ecommerce.orderservice.repository.*;
import com.ecommerce.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductClient productClient;
    private final OrderEventProducer eventProducer;

    @Override
    @Transactional
    public OrderResponse placeOrder(String userId, PlaceOrderRequest request) {
        var cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot place order with empty cart");
        }

        // Build order items from cart (price snapshot)
        var orderItems = cart.getItems().stream().map(ci ->
            OrderItem.builder()
                .productId(ci.getProductId())
                .productName(ci.getProductName())
                .unitPrice(ci.getUnitPrice())
                .quantity(ci.getQuantity())
                .build()
        ).toList();

        var order = Order.builder()
            .userId(userId)
            .totalAmount(cart.getTotalAmount())
            .deliveryStreet(request.deliveryStreet())
            .deliveryCity(request.deliveryCity())
            .deliveryState(request.deliveryState())
            .deliveryZipCode(request.deliveryZipCode())
            .deliveryCountry(request.deliveryCountry())
            .build();

        orderItems.forEach(item -> {
            item.setOrder(order);
            order.getItems().add(item);
        });

        var saved = orderRepository.save(order);

        // Clear cart after order placed
        cart.getItems().clear();
        cartRepository.save(cart);

        // Publish Kafka event
        var event = new OrderPlacedEvent(
            saved.getId(), userId,
            saved.getTotalAmount(),
            orderItems.stream()
                .map(i -> new OrderItemDto(i.getProductId(), i.getProductName(),
                        i.getUnitPrice(), i.getQuantity()))
                .toList(),
            LocalDateTime.now()
        );
        eventProducer.publishOrderPlaced(event);
        log.info("Order placed: {} for user: {}", saved.getId(), userId);

        return OrderResponse.from(saved);
    }

    @Override
    public List<OrderResponse> getOrdersByUser(String userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
            .map(OrderResponse::from)
            .toList();
    }

    @Override
    public OrderResponse getOrderById(String orderId, String userId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
            .map(OrderResponse::from)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
    }

    @Override
    @Transactional
    public void updateOrderStatus(String orderId, OrderStatus newStatus) {
        var order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        order.setStatus(newStatus);
        orderRepository.save(order);
        log.info("Order {} status updated to: {}", orderId, newStatus);
    }
}
