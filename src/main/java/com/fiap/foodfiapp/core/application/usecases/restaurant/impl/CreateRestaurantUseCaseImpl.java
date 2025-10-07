package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.application.usecases.restaurant.CreateRestaurantUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedAccessException;
import com.fiap.foodfiapp.core.domain.exception.UserNotFoundException;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;

import java.util.UUID;

public class CreateRestaurantUseCaseImpl implements CreateRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public CreateRestaurantUseCaseImpl(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Restaurant execute(UUID authenticatedUserId, Restaurant createRestaurant) {
        // RN14: validar existência do usuário (owner)
        User owner = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new UserNotFoundException("id", String.valueOf(authenticatedUserId)));

        // RN14: validar papel do usuário (deve ser OWNER / Dono de Restaurante)
        String userTypeName = owner.getUserType() != null ? owner.getUserType().getName() : null;
        boolean isOwner = "OWNER".equalsIgnoreCase(userTypeName) || "DONO DE RESTAURANTE".equalsIgnoreCase(userTypeName);
        if (!isOwner) {
            throw new UnauthorizedAccessException("Only users with role 'OWNER' can create restaurants");
        }

        // Vincular o owner autenticado ao restaurante
        createRestaurant.setUserOwnerId(authenticatedUserId);

        // RN17: Não permitir restaurantes duplicados por dono (nome + owner)
        Restaurant existing = restaurantRepository.findByNameAndUser(createRestaurant.getName(), authenticatedUserId);
        if (existing != null) {
            throw new BusinessException("A restaurant with name '" + createRestaurant.getName() + "' already exists for this owner");
        }

        return this.restaurantRepository.save(createRestaurant);
    }
}
