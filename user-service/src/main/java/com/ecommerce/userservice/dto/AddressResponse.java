package com.ecommerce.userservice.dto;


import com.ecommerce.userservice.entity.Address;

public record AddressResponse(
        String id, String street, String city,
        String state, String zipCode, String country, boolean isDefault
) {
    public static AddressResponse from(Address a) {
        return new AddressResponse(a.getId(), a.getStreet(), a.getCity(),
                a.getState(), a.getZipCode(), a.getCountry(), a.isDefault());
    }
}
