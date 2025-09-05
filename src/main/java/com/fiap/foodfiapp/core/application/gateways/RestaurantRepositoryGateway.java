package com.fiap.foodfiapp.core.application.gateways;

import com.fiap.foodfiapp.core.domain.entities.CreateRestaurant;
import com.fiap.foodfiapp.core.domain.entities.Restaurant;

import java.util.UUID;

public interface RestaurantRepositoryGateway {
    Restaurant findById(UUID id);
    Restaurant createRestaurant(CreateRestaurant createRestaurant);
}
