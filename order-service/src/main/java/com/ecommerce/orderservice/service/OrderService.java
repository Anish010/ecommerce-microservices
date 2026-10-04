package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.OrderResponse;
import com.ecommerce.orderservice.dto.PlaceOrderRequest;
import com.ecommerce.orderservice.entity.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(String userId, PlaceOrderRequest request);
    List<OrderResponse> getOrdersByUser(String userId);
    OrderResponse getOrderById(String orderId, String userId);
    void updateOrderStatus(String orderId, OrderStatus newStatus);
}
