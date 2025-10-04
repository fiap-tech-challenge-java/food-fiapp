package com.fiap.foodfiapp.core.application.usecases.addressesrestaurant.impl;

import com.fiap.foodfiapp.core.application.gateways.AddressesRestaurantRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.addressesrestaurant.CreateAddressesRestaurantUseCase;
import com.fiap.foodfiapp.core.domain.entities.addressesrestaurant.CreateAddressesRestaurant;
import com.fiap.foodfiapp.core.domain.entities.addressesrestaurant.CreatedAddressesRestaurant;
import org.springframework.stereotype.Service;

@Service
public class CreateAddressesRestaurantUseCaseImpl implements CreateAddressesRestaurantUseCase {
    private final AddressesRestaurantRepositoryGateway addressesRestaurantRepositoryGateway;

    public CreateAddressesRestaurantUseCaseImpl(AddressesRestaurantRepositoryGateway addressesRestaurantRepositoryGateway) {
        this.addressesRestaurantRepositoryGateway = addressesRestaurantRepositoryGateway;
    }

    @Override
    public CreatedAddressesRestaurant execute(CreateAddressesRestaurant createAddressesRestaurant) {
        return this.addressesRestaurantRepositoryGateway.createAddressesRestaurant(createAddressesRestaurant);
    }
}
