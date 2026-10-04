package com.ecommerce.productservice.dto;

import java.io.Serializable;
import java.util.List;

public record ProductListResponse(List<ProductResponse> products) implements Serializable {
}