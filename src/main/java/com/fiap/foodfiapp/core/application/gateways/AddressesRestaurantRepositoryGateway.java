package com.fiap.foodfiapp.core.application.gateways;

import com.fiap.foodfiapp.core.domain.entity.addressesrestaurant.CreateAddressesRestaurant;

import java.util.UUID;

public interface AddressesRestaurantRepositoryGateway {
    CreatedAddressesRestaurant createAddressesRestaurant(CreateAddressesRestaurant createdAddressesRestaurant);
    void deleteAddressesRestaurant(UUID restaurantId);
}
