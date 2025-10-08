// src/main/java/com/fiap/foodfiapp/core/application/usecases/restaurant/impl/UpdateRestaurantUseCaseImpl.java
package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.application.usecases.restaurant.UpdateRestaurantUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedAccessException;

import java.util.UUID;

public class UpdateRestaurantUseCaseImpl implements UpdateRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;

    public UpdateRestaurantUseCaseImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Restaurant execute(UUID authenticatedUserId, UUID restaurantId, Restaurant restaurantUpdates) {
        // 1. Verificação de existência do restaurante
        Restaurant existingRestaurant = restaurantRepository.findById(restaurantId);
        if (existingRestaurant == null) {
            throw new BusinessException("Restaurante não encontrado.");
        }

        // 2. Verificação de posse do restaurante
        if (!authenticatedUserId.equals(existingRestaurant.getUserOwnerId())) {
            throw new UnauthorizedAccessException("Permissão negada. Você só pode editar seus próprios restaurantes.");
        }

        // 3. Aplica as atualizações da entidade de domínio na entidade existente
        if (restaurantUpdates.getName() != null) {
            existingRestaurant.setName(restaurantUpdates.getName());
        }
        if (restaurantUpdates.getCuisineType() != null) {
            existingRestaurant.setCuisineType(restaurantUpdates.getCuisineType());
        }
        if (restaurantUpdates.getOpeningHours() != null) {
            existingRestaurant.setOpeningHours(restaurantUpdates.getOpeningHours());
        }
        // Nota: A lógica para atualizar o endereço seria mais complexa e
        // provavelmente necessitaria do seu próprio caso de uso.

        return this.restaurantRepository.update(existingRestaurant);
    }
}