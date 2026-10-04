package com.ecommerce.orderservice.service.impl;

import com.ecommerce.orderservice.client.ProductClient;
import com.ecommerce.orderservice.dto.*;
import com.ecommerce.orderservice.entity.*;
import com.ecommerce.orderservice.exception.ResourceNotFoundException;
import com.ecommerce.orderservice.repository.*;
import com.ecommerce.orderservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductClient productClient;

    private Cart getOrCreateCart(String userId) {
        return cartRepository.findByUserId(userId)
            .orElseGet(() -> cartRepository.save(
                Cart.builder().userId(userId).build()
            ));
    }

    @Override
    public CartResponse getCart(String userId) {
        var cart = getOrCreateCart(userId);
        return CartResponse.from(cart);
    }

    @Override
    public CartResponse addItem(String userId, AddToCartRequest request) {
        var cart    = getOrCreateCart(userId);
        var product = productClient.getProductById(request.productId());

        // If item exists, increment quantity
        var existing = cart.getItems().stream()
            .filter(i -> i.getProductId().equals(request.productId()))
            .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + request.quantity());
        } else {
            cart.getItems().add(CartItem.builder()
                .cart(cart)
                .productId(product.id())
                .productName(product.name())
                .unitPrice(product.price())
                .quantity(request.quantity())
                .build()
            );
        }
        return CartResponse.from(cartRepository.save(cart));
    }

    @Override
    public CartResponse removeItem(String userId, String cartItemId) {
        var cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        cart.getItems().removeIf(i -> i.getId().equals(cartItemId));
        return CartResponse.from(cartRepository.save(cart));
    }

    @Override
    public void clearCart(String userId) {
        var cart = getOrCreateCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
