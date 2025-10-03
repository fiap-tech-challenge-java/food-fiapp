package com.fiap.foodfiapp.core.application.usecases.addressesrestaurant;

import com.fiap.foodfiapp.core.domain.entities.addressesrestaurant.CreateAddressesRestaurant;
import com.fiap.foodfiapp.core.domain.entities.addressesrestaurant.CreatedAddressesRestaurant;

public interface CreateAddressesRestaurantUseCase {
    CreatedAddressesRestaurant execute(CreateAddressesRestaurant createAddressesRestaurant);
}
