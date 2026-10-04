package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.AddToCartRequest;
import com.ecommerce.orderservice.dto.CartResponse;

public interface CartService {
    CartResponse getCart(String userId);
    CartResponse addItem(String userId, AddToCartRequest request);
    CartResponse removeItem(String userId, String cartItemId);
    void clearCart(String userId);
}
