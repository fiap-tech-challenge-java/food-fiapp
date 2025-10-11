package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.application.usecases.restaurant.CreateRestaurantUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.RestaurantNameAlreadyExistsException;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedAccessException;
import com.fiap.foodfiapp.core.domain.exception.UserNotFoundException;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.validator.AddressValidator;
import com.fiap.foodfiapp.core.domain.validator.RestaurantValidator;

import java.util.UUID;

public class CreateRestaurantUseCaseImpl implements CreateRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public CreateRestaurantUseCaseImpl(RestaurantRepository restaurantRepository, UserRepository userRepository, AddressRepository addressRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public Restaurant execute(UUID userId, Restaurant restaurant) {
        RestaurantValidator.validate(restaurant);

        restaurant.setUserOwnerId(userId);

        if (restaurant.getName() == null || restaurant.getName().trim().isEmpty()) {
            throw new BusinessException("Restaurant name is required");
        }

        UUID authenticatedUserId = restaurant.getUserOwnerId();

        User owner = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + authenticatedUserId));

        String userTypeName = owner.getUserType() != null ? owner.getUserType().getName() : null;
        boolean isAdmin = "ADMIN".equalsIgnoreCase(userTypeName);

        boolean isOwner = "OWNER".equalsIgnoreCase(userTypeName) || "DONO DE RESTAURANTE".equalsIgnoreCase(userTypeName);

        if (!isAdmin && !isOwner) {
            throw new UnauthorizedAccessException("Only users with role 'OWNER' or 'ADMIN' can create restaurants");
        }

        Restaurant existing = restaurantRepository.findByNameAndUser(restaurant.getName(), authenticatedUserId);
        if (existing != null) {
            throw new BusinessException("A restaurant with name '" + restaurant.getName() + "' already exists for this owner");
        }

        if (restaurant.getAddress() != null) {
            AddressValidator.validate(restaurant.getAddress());
        }

        restaurant.setIsActive(true);

        var savedRestaurant = this.restaurantRepository.save(restaurant);

        if (restaurant.getAddress() != null) {
            var address = restaurant.getAddress();
            var savedAddress = addressRepository.save(address, savedRestaurant.getId(), AddressOwnerTypeEnum.RESTAURANT.getDescription());
            savedRestaurant.setAddress(savedAddress);
        }


        return savedRestaurant;
    }
}