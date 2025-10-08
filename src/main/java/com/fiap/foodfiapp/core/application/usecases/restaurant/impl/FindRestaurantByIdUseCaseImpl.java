package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.application.usecases.restaurant.FindRestaurantByIdUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;

import java.util.UUID;

public class FindRestaurantByIdUseCaseImpl implements FindRestaurantByIdUseCase {
    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;

    public FindRestaurantByIdUseCaseImpl(RestaurantRepository restaurantRepository, AddressRepository addressRepository) {
        this.restaurantRepository = restaurantRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public Restaurant execute(UUID id) {
        var restaurant = this.restaurantRepository.findById(id);

        if (restaurant != null) {
            var addresses = addressRepository.findByOwner(id, AddressOwnerTypeEnum.RESTAURANT.getDescription());
            if (!addresses.isEmpty()) {
                restaurant.setAddress(addresses.get(0));
            }
        }

        return restaurant;
    }
}