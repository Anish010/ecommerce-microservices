package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductListResponse;
import com.ecommerce.productservice.dto.ProductRequest;
import com.ecommerce.productservice.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductListResponse getAllProducts();

    ProductResponse getProductById(String id);

    Page<ProductResponse> getProductsByCategory(String categoryId, Pageable pageable);

    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(String id, ProductRequest request);

    void deleteProduct(String id);

    void decreaseStock(String productId, int quantity);
}
