package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.Address;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.model.CreateRestaurantRequest;
import com.fiap.foodfiapp.model.RestaurantResponse;
import com.fiap.foodfiapp.model.UpdateRestaurantRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {UserMapper.class})
public interface RestaurantMapper {

    RestaurantMapper INSTANCE = Mappers.getMapper(RestaurantMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "active", ignore = true)
    Restaurant toRestaurant(CreateRestaurantRequest createRestaurantRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "userOwnerId", ignore = true)
    Restaurant toRestaurant(UpdateRestaurantRequest updateRestaurantRequest);

    @Mapping(source = "address", target = "address", qualifiedByName = "addressToString")
    @Mapping(source = "active", target = "isActive")
    @Mapping(target = "owner", ignore = true)
    RestaurantResponse toRestaurantResponse(Restaurant restaurant);

    List<RestaurantResponse> toRestaurantResponseList(List<Restaurant> restaurants);

    @Named("addressToString")
    default String addressToString(Address address) {
        if (address == null) {
            return null;
        }
        // Formata o endereço em uma única string
        return String.format("%s, %s - %s, %s - %s, %s",
                address.getPublicPlace(), address.getNumber(), address.getNeighborhood(),
                address.getCity(), address.getState(), address.getPostalCode());
    }
}