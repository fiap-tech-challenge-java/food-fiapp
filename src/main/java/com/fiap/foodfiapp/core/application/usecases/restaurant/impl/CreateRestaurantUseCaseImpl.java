// src/main/java/com/fiap/foodfiapp/core/application/usecases/restaurant/impl/CreateRestaurantUseCaseImpl.java
package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.application.usecases.restaurant.CreateRestaurantUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedAccessException;
import com.fiap.foodfiapp.core.domain.exception.UserNotFoundException;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;

import java.util.UUID;

public class CreateRestaurantUseCaseImpl implements CreateRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository; // Adicionado

    public CreateRestaurantUseCaseImpl(RestaurantRepository restaurantRepository, UserRepository userRepository, AddressRepository addressRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository; // Adicionado
    }

    @Override
    public Restaurant execute(UUID userId, Restaurant restaurant) {
        // Set the user owner ID internally - this is now handled by the use case
        restaurant.setUserOwnerId(userId);

        UUID authenticatedUserId = restaurant.getUserOwnerId();

        // RN14: validar existência do usuário (owner)
        User owner = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new UserNotFoundException("id", String.valueOf(authenticatedUserId)));

        // RN14: validar papel do usuário (deve ser OWNER / Dono de Restaurante)
        String userTypeName = owner.getUserType() != null ? owner.getUserType().getName() : null;
        boolean isOwner = "OWNER".equalsIgnoreCase(userTypeName) || "DONO DE RESTAURANTE".equalsIgnoreCase(userTypeName);
        if (!isOwner) {
            throw new UnauthorizedAccessException("Only users with role 'OWNER' can create restaurants");
        }

        // RN17: Não permitir restaurantes duplicados por dono (nome + owner)
        Restaurant existing = restaurantRepository.findByNameAndUser(restaurant.getName(), authenticatedUserId);
        if (existing != null) {
            throw new BusinessException("A restaurant with name '" + restaurant.getName() + "' already exists for this owner");
        }

        // Definir campos padrão
        restaurant.setActive(true);

        var savedRestaurant = this.restaurantRepository.save(restaurant);

        // Salvar o endereço associado ao restaurante
        if (restaurant.getAddress() != null) {
            var address = restaurant.getAddress();
            var savedAddress = addressRepository.save(address, savedRestaurant.getId(), AddressOwnerTypeEnum.RESTAURANT.getDescription());
            savedRestaurant.setAddress(savedAddress);
        }


        return savedRestaurant;
    }
}