package com.fiap.foodfiapp.core.application.usecases.addressesrestaurant.impl;

import com.fiap.foodfiapp.core.application.gateways.AddressesRestaurantRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.addressesrestaurant.DeleteAddressesRestaurantUseCase;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteAddressesRestaurantUseCaseImpl implements DeleteAddressesRestaurantUseCase {
    private final AddressesRestaurantRepositoryGateway addressesRestaurantRepositoryGateway;

    public DeleteAddressesRestaurantUseCaseImpl(AddressesRestaurantRepositoryGateway addressesRestaurantRepositoryGateway) {
        this.addressesRestaurantRepositoryGateway = addressesRestaurantRepositoryGateway;
    }

    @Override
    public void deleteAddressesRestaurant(UUID restaurantId) {
        this.addressesRestaurantRepositoryGateway.deleteAddressesRestaurant(restaurantId);
    }
}
