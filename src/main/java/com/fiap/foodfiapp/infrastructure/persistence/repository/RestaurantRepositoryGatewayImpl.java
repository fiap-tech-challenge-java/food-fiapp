package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.application.gateways.RestaurantRepositoryGateway;
import com.fiap.foodfiapp.core.domain.entities.restaurant.CreateRestaurant;
import com.fiap.foodfiapp.core.domain.entities.restaurant.CreatedRestaurant;
import com.fiap.foodfiapp.core.domain.entities.restaurant.Restaurant;
import com.fiap.foodfiapp.core.domain.entities.restaurant.UpdateRestaurant;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
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
                .orElseThrow(() -> new BusinessException("User not found"));

        if(userEntity.getUserType().getId() == 2){
            boolean jaExisteRestaurante = userEntity.getRestaurants().stream()
                    .anyMatch(r -> r.getName().equalsIgnoreCase(createRestaurant.name()));

            if(!jaExisteRestaurante){
                RestaurantEntity restaurantEntity = new RestaurantEntity(
                        createRestaurant.name(),
                        createRestaurant.cuisineType(),
                        createRestaurant.openingHours(),
                        createRestaurant.userId(),
                        null
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
        RestaurantEntity entity = this.restaurantSpringDataRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Restaurant not found"));

        return RESTAURANT_MAPPER.mapToRestaurant(entity);
    }

    @Override
    public Restaurant findByName(String name, UUID userId) {
        RestaurantEntity entity = this.restaurantSpringDataRepository.findByUserIdAndName(userId, name)
                .orElseThrow(() -> new BusinessException("User's restaurant not found"));

        return RESTAURANT_MAPPER.mapToRestaurant(entity);
    }

    @Override
    public List<Restaurant> findAllByUserId(UUID userId) {
        List<RestaurantEntity> entities = this.restaurantSpringDataRepository.findAllByUserId(userId)
                .orElseThrow(() -> new BusinessException("User's restaurant not found"));

        return RESTAURANT_MAPPER.mapToListRestaurant(entities);
    }

    @Override
    public Restaurant updateRestaurant(UpdateRestaurant updateRestaurant) {
        UserEntity userEntity = userSpringDataRepository.findById(updateRestaurant.getUserId())
                .orElseThrow(() -> new BusinessException("User not found"));

        if(userEntity.getUserType().getId() == 2) {
            RestaurantEntity restaurantEntity = this.restaurantSpringDataRepository.findById(updateRestaurant.getId())
                    .orElseThrow(() -> new BusinessException("Restaurant not found"));

            restaurantEntity.setCuisineType(updateRestaurant.getCuisineType());
            restaurantEntity.setName(updateRestaurant.getName());
            restaurantEntity.setOpeningHours(updateRestaurant.getOpeningHours());
            restaurantEntity.setUserId(updateRestaurant.getUserId()); // Trocar restaurant de dono

            restaurantSpringDataRepository.save(restaurantEntity);
            return RESTAURANT_MAPPER.mapToRestaurant(restaurantEntity);
        }

        throw new BusinessException("New user not permitted");
    }
}
