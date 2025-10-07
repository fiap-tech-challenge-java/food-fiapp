package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.model.CreateRestaurantRequest;
import com.fiap.foodfiapp.model.RestaurantResponse;
import com.fiap.foodfiapp.model.UpdateRestaurantRequest;
import com.fiap.foodfiapp.model.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {AddressMapper.class, UserMapper.class})
public interface RestaurantMapper {

    RestaurantMapper INSTANCE = Mappers.getMapper(RestaurantMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "address", ignore = true) // Endereço será tratado separadamente
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    Restaurant toRestaurant(CreateRestaurantRequest createRestaurantRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "userOwnerId", ignore = true) // O dono não deve ser alterado nesta operação
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    Restaurant toRestaurant(UpdateRestaurantRequest updateRestaurantRequest);

    // O campo 'owner' do tipo UserResponse será populado no controller
    @Mapping(target = "owner", ignore = true)
    @Mapping(source = "active", target = "isActive")
    RestaurantResponse toRestaurantResponse(Restaurant restaurant);

    List<RestaurantResponse> toRestaurantResponseList(List<Restaurant> restaurants);
}