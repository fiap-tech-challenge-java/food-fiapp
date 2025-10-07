package com.fiap.foodfiapp.core.domain.port;

import com.fiap.foodfiapp.core.domain.entity.Restaurant;

import java.util.List;
import java.util.UUID;

public interface RestaurantRepository {
    Restaurant save(Restaurant restaurant);
    Restaurant findById(UUID id);
    boolean existsById(UUID id);
    Restaurant findByNameAndUser(String name, UUID userId);
    Restaurant changeOwner(UUID restaurantId, UUID newOwnerId);
    List<Restaurant> findAllByUserId(UUID userId);
    void delete(UUID id);
    Restaurant update(Restaurant restaurant);
}