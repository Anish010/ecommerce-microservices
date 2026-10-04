package com.ecommerce.productservice.dto;

import com.ecommerce.productservice.entity.Category;

import java.io.Serializable;

public record CategoryResponse(
        String id,
        String name,
        String description,
        long productCount
) implements Serializable {

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getProducts() != null ? category.getProducts().size() : 0
        );
    }
}
