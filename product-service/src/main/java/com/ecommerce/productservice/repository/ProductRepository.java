package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findByActiveTrue();

    Page<Product> findByCategoryIdAndActiveTrue(String categoryId, Pageable pageable);

    boolean existsByNameIgnoreCase(String name);
}
