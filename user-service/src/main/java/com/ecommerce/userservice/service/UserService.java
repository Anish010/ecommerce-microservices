package com.ecommerce.userservice.service;

import com.ecommerce.userservice.dto.*;
import java.util.List;

public interface UserService {
    UserResponse getUserById(String id);
    UserResponse getUserByEmail(String email);
    UserResponse updateUser(String id, UpdateUserRequest request);
    List<UserResponse> getAllUsers();
    void deleteUser(String id);
    AddressResponse addAddress(String userId, AddressRequest request);
    List<AddressResponse> getAddresses(String userId);
}
