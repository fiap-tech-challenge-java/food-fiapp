package com.fiap.foodfiapp.core.application.gateways;

import com.fiap.foodfiapp.core.domain.entities.addressesrestaurant.CreateAddressesRestaurant;
import com.fiap.foodfiapp.core.domain.entities.addressesrestaurant.CreatedAddressesRestaurant;

public interface AddressesRestaurantRepositoryGateway {
    CreatedAddressesRestaurant createAddressesRestaurant(CreateAddressesRestaurant createdAddressesRestaurant);
}
