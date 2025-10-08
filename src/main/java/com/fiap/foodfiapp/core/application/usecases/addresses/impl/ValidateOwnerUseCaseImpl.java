package com.fiap.foodfiapp.core.application.usecases.addresses.impl;

import com.fiap.foodfiapp.core.application.usecases.addresses.ValidateOwnerUseCase;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import com.fiap.foodfiapp.core.domain.exception.UserNotFoundException;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedAccessException;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;

import java.util.UUID;

public class ValidateOwnerUseCaseImpl implements ValidateOwnerUseCase {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public ValidateOwnerUseCaseImpl(UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public void execute(UUID ownerId, String ownerType) {
        if (AddressOwnerTypeEnum.USER.getDescription().equals(ownerType)) {
            validateUserOwner(ownerId);
        } else if (AddressOwnerTypeEnum.RESTAURANT.getDescription().equals(ownerType)) {
            validateRestaurantOwner(ownerId);
        } else {
            throw new IllegalArgumentException("Invalid owner type: " + ownerType);
        }
    }

    private void validateUserOwner(UUID ownerId) {
        var user = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("Owner (user) not found or inactive."));

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new UserNotFoundException("Owner (user) not found or inactive.");
        }

        if (user.getUserType() == null || !"CUSTOMER".equals(user.getUserType().getName())) {
            throw new UnauthorizedAccessException("User does not have permission to manage addresses.");
        }
    }

    private void validateRestaurantOwner(UUID ownerId) {
        var restaurant = restaurantRepository.findById(ownerId);
        if (restaurant == null) {
            throw new BusinessException("Owner (restaurant) not found or inactive.");
        }
    }
}
