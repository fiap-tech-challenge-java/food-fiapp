package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.application.gateways.RestaurantRepositoryGateway;
import com.fiap.foodfiapp.core.domain.entities.restaurant.CreateRestaurant;
import com.fiap.foodfiapp.core.domain.entities.restaurant.CreatedRestaurant;
import com.fiap.foodfiapp.core.domain.entities.restaurant.Restaurant;
import com.fiap.foodfiapp.core.domain.entities.restaurant.UpdateRestaurant;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.NotFoundException;
import com.fiap.foodfiapp.infrastructure.persistence.entity.RestaurantEntity;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserEntity;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.RestaurantSpringDataRepository;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.UserSpringDataRepository;
import com.fiap.foodfiapp.infrastructure.rest.mapper.RestaurantMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RestaurantRepositoryGatewayImpl implements RestaurantRepositoryGateway {
    private final UserSpringDataRepository userSpringDataRepository;
    private final RestaurantSpringDataRepository restaurantSpringDataRepository;
    private static final RestaurantMapper RESTAURANT_MAPPER = RestaurantMapper.INSTANCE;

    public RestaurantRepositoryGatewayImpl(UserSpringDataRepository userSpringDataRepository,
                                           RestaurantSpringDataRepository restaurantSpringDataRepository) {
        this.userSpringDataRepository = userSpringDataRepository;
        this.restaurantSpringDataRepository = restaurantSpringDataRepository;
    }

    @Override
    public CreatedRestaurant createRestaurant(CreateRestaurant createRestaurant) {
        UserEntity userEntity = userSpringDataRepository.findById(createRestaurant.userId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(userEntity.getUserType().getId() == 2){
            boolean jaExisteRestaurante = userEntity.getRestaurants().stream()
                    .anyMatch(r -> r.getName().equalsIgnoreCase(createRestaurant.name()));

            if(!jaExisteRestaurante){
                RestaurantEntity restaurantEntity = new RestaurantEntity(
                        createRestaurant.name(),
                        createRestaurant.cuisineType(),
                        createRestaurant.openingHours(),
                        createRestaurant.userId(),
                        null,
                        true
                );

                restaurantSpringDataRepository.save(restaurantEntity);
                return RESTAURANT_MAPPER.mapToCreatedRestaurant(restaurantEntity);
            }

            throw new BusinessException("Restaurant already created with this name for this user");
        }

        throw new BusinessException("User not permitted");
    }

    @Override
    public Restaurant findById(UUID id) {
        RestaurantEntity entity = this.restaurantSpringDataRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        return RESTAURANT_MAPPER.mapToRestaurant(entity);
    }

    @Override
    public Restaurant findByName(String name, UUID userId) {
        RestaurantEntity entity = this.restaurantSpringDataRepository.findByUserIdAndNameAndActiveTrue(userId, name)
                .orElseThrow(() -> new NotFoundException("User's restaurant not found"));

        return RESTAURANT_MAPPER.mapToRestaurant(entity);
    }

    @Override
    public List<Restaurant> findAllByUserId(UUID userId) {
        List<RestaurantEntity> entities = this.restaurantSpringDataRepository.findAllByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new NotFoundException("User's restaurant not found"));

        return RESTAURANT_MAPPER.mapToListRestaurant(entities);
    }

    @Override
    public Restaurant updateRestaurant(UpdateRestaurant updateRestaurant) {
        UserEntity userEntity = userSpringDataRepository.findById(updateRestaurant.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(userEntity.getUserType().getId() == 2) {
            RestaurantEntity restaurantEntity = this.restaurantSpringDataRepository.findByIdAndActiveTrue(updateRestaurant.getId())
                    .orElseThrow(() -> new NotFoundException("Restaurant not found"));

            restaurantEntity.setCuisineType(updateRestaurant.getCuisineType());
            restaurantEntity.setName(updateRestaurant.getName());
            restaurantEntity.setOpeningHours(updateRestaurant.getOpeningHours());
            restaurantSpringDataRepository.save(restaurantEntity);

            return RESTAURANT_MAPPER.mapToRestaurant(restaurantEntity);
        }

        throw new BusinessException("User not Owner");
    }

    @Override
    public void deleteRestaurant(UUID id) {
        RestaurantEntity restaurantEntity = this.restaurantSpringDataRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        restaurantEntity.setActive(false);
        restaurantSpringDataRepository.save(restaurantEntity);
    }

    @Override
    public Restaurant changeOwner(UUID restaurantId, UUID newOwnerId) {
        RestaurantEntity restaurantEntity = this.restaurantSpringDataRepository.findByIdAndActiveTrue(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        UserEntity userEntity = userSpringDataRepository.findById(newOwnerId)
                .orElseThrow(() -> new NotFoundException("New Owner not found"));

        if(userEntity.getUserType().getId() == 2) {
            restaurantEntity.setUserId(newOwnerId);
            restaurantSpringDataRepository.save(restaurantEntity);

            return RESTAURANT_MAPPER.mapToRestaurant(restaurantEntity);
        }

        throw new BusinessException("New user not Owner");
    }

}
