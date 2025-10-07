package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.application.usecases.restaurant.CreateRestaurantUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedAccessException;
import com.fiap.foodfiapp.core.domain.exception.UserNotFoundException;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.infrastructure.persistence.enums.AddressOwnerTypeEnum;

import java.util.UUID;

public class CreateRestaurantUseCaseImpl implements CreateRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository; // Repositório de endereço injetado

    public CreateRestaurantUseCaseImpl(RestaurantRepository restaurantRepository, UserRepository userRepository, AddressRepository addressRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
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

        Restaurant savedRestaurant = this.restaurantRepository.save(createRestaurant);

        // Salva o endereço associado ao restaurante
        if (createRestaurant.getAddress() != null) {
            var address = createRestaurant.getAddress();
            var savedAddress = addressRepository.save(address, savedRestaurant.getId(), AddressOwnerTypeEnum.RESTAURANT.getDescription());
            savedRestaurant.setAddress(savedAddress);
        }

        return savedRestaurant;

    }
}
