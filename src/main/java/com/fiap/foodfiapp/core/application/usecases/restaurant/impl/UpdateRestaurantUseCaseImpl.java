package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.application.usecases.restaurant.UpdateRestaurantUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedAccessException;
import com.fiap.foodfiapp.core.domain.validator.RestaurantValidator;

import java.util.UUID;

public class UpdateRestaurantUseCaseImpl implements UpdateRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public UpdateRestaurantUseCaseImpl(RestaurantRepository restaurantRepository, AddressRepository addressRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Restaurant execute(UUID authenticatedUserId, UUID restaurantId, Restaurant restaurantUpdates) {
        // Validate individual fields that are being updated
        if (restaurantUpdates.getName() != null) {
            RestaurantValidator.validateName(restaurantUpdates.getName());
        }
        if (restaurantUpdates.getCuisineType() != null) {
            RestaurantValidator.validateCuisineType(restaurantUpdates.getCuisineType());
        }
        if (restaurantUpdates.getOpeningHours() != null) {
            RestaurantValidator.validateOpeningHours(restaurantUpdates.getOpeningHours());
        }
        if (restaurantUpdates.getDescription() != null) {
            RestaurantValidator.validateDescription(restaurantUpdates.getDescription());
        }

        restaurantUpdates.setId(restaurantId);
        restaurantUpdates.setUserOwnerId(authenticatedUserId);

        Restaurant existingRestaurant = restaurantRepository.findById(restaurantId);
        if (existingRestaurant == null) {
            throw new BusinessException("Restaurant not found.");
        }

        // Check if user is ADMIN
        User user = userRepository.findById(authenticatedUserId).orElse(null);
        boolean isAdmin = false;
        if (user != null && user.getUserType() != null) {
            String userTypeName = user.getUserType().getName();
            isAdmin = "ADMIN".equalsIgnoreCase(userTypeName);
        }

        // ADMIN can edit any restaurant, others can only edit their own
        if (!isAdmin && !authenticatedUserId.equals(existingRestaurant.getUserOwnerId())) {
            throw new UnauthorizedAccessException("Permission denied. You can only edit your own restaurants.");
        }

        if (restaurantUpdates.getName() != null) {
            existingRestaurant.setName(restaurantUpdates.getName());
        }
        if (restaurantUpdates.getCuisineType() != null) {
            existingRestaurant.setCuisineType(restaurantUpdates.getCuisineType());
        }
        if (restaurantUpdates.getOpeningHours() != null) {
            existingRestaurant.setOpeningHours(restaurantUpdates.getOpeningHours());
        }
        if (restaurantUpdates.getDescription() != null) {
            existingRestaurant.setDescription(restaurantUpdates.getDescription());
        }

        // LÓGICA PARA ATUALIZAR O ENDEREÇO
        if (restaurantUpdates.getAddress() != null) {
            var addresses = addressRepository.findByOwner(restaurantId, AddressOwnerTypeEnum.RESTAURANT.getDescription());
            if (!addresses.isEmpty()) {
                var existingAddress = addresses.get(0);
                var addressToUpdate = restaurantUpdates.getAddress();
                addressToUpdate.setId(existingAddress.getId()); // Garante que estamos atualizando o endereço certo
                var savedAddress = addressRepository.save(addressToUpdate, restaurantId, AddressOwnerTypeEnum.RESTAURANT.getDescription());
                existingRestaurant.setAddress(savedAddress);
            } else {
                // Caso o restaurante não tenha endereço, cria um novo
                var savedAddress = addressRepository.save(restaurantUpdates.getAddress(), restaurantId, AddressOwnerTypeEnum.RESTAURANT.getDescription());
                existingRestaurant.setAddress(savedAddress);
            }
        }

        // Salva as alterações do restaurante no banco de dados
        this.restaurantRepository.update(existingRestaurant);

        // Retorna o objeto que temos em memória, que já contém o endereço atualizado
        return existingRestaurant;
    }
}