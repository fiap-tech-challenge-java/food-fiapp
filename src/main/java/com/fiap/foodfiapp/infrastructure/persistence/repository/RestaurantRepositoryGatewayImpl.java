package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.application.gateways.RestaurantRepositoryGateway;
import com.fiap.foodfiapp.core.domain.entities.CreateRestaurant;
import com.fiap.foodfiapp.core.domain.entities.Restaurant;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserEntity;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.RestaurantSpringDataRepository;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.UserSpringDataRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RestaurantRepositoryGatewayImpl implements RestaurantRepositoryGateway {
    private final UserSpringDataRepository userSpringDataRepository;
    private final RestaurantSpringDataRepository restaurantSpringDataRepository;

    public RestaurantRepositoryGatewayImpl(UserSpringDataRepository userSpringDataRepository,
                                           RestaurantSpringDataRepository restaurantSpringDataRepository) {
        this.userSpringDataRepository = userSpringDataRepository;
        this.restaurantSpringDataRepository = restaurantSpringDataRepository;
    }


    @Override
    public Restaurant findById(UUID id) {
        return null;
    }

    @Override
    public Restaurant createRestaurant(CreateRestaurant createRestaurant) {
        UserEntity userEntity = userSpringDataRepository.findById(createRestaurant.idUserOwner())
                .orElseThrow(() -> new BusinessException("User not found"));

        if(userEntity.getUserType().toString() == "2"){
            // cadastrar restaurante para user owner
        }

        throw new BusinessException("User not permitted");
    }
}
