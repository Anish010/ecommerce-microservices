package com.ecommerce.orderservice.client;

import com.ecommerce.orderservice.dto.ProductDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    @CircuitBreaker(name = "productService", fallbackMethod = "getProductFallback")
    ProductDto getProductById(@PathVariable String id);

    @PutMapping("/api/products/{id}/decrease-stock")
    @CircuitBreaker(name = "productService")
    void decreaseStock(@PathVariable String id, @RequestParam int quantity);

    default ProductDto getProductFallback(String id, Exception ex) {
        throw new RuntimeException("Product service unavailable: " + id);
    }
}
