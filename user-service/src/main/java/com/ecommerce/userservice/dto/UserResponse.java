package com.ecommerce.userservice.dto;

import com.ecommerce.userservice.entity.User;

public record UserResponse(String id, String name, String email, String role) {
    public static UserResponse from(User u) {
        return new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole().name());
    }
}
