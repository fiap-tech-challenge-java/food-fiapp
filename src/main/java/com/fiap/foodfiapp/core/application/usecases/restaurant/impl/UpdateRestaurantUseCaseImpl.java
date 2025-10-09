package com.fiap.foodfiapp.core.application.usecases.restaurant.impl;

import com.fiap.foodfiapp.core.domain.enums.AddressOwnerTypeEnum;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.application.usecases.restaurant.UpdateRestaurantUseCase;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedAccessException;

import java.util.UUID;

public class UpdateRestaurantUseCaseImpl implements UpdateRestaurantUseCase {
    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;

    public UpdateRestaurantUseCaseImpl(RestaurantRepository restaurantRepository, AddressRepository addressRepository) {
        this.restaurantRepository = restaurantRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public Restaurant execute(UUID authenticatedUserId, UUID restaurantId, Restaurant restaurantUpdates) {
        if (restaurantId == null) {
            throw new NullPointerException("Restaurant ID cannot be null");
        }
        if (restaurantUpdates == null) {
            throw new NullPointerException("Restaurant updates cannot be null");
        }

        Restaurant existingRestaurant = restaurantRepository.findById(restaurantId);
        if (existingRestaurant == null) {
            throw new BusinessException("Restaurante não encontrado.");
        }

        if (!authenticatedUserId.equals(existingRestaurant.getUserOwnerId())) {
            throw new UnauthorizedAccessException("Permissão negada. Você só pode editar seus próprios restaurantes.");
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