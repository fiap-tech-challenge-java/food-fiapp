package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.model.CreateRestaurantRequest;
import com.fiap.foodfiapp.model.RestaurantResponse;
import com.fiap.foodfiapp.model.UpdateRestaurantRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Collections;

@Mapper(uses = {UserMapper.class, AddressesMapper.class})
public interface RestaurantMapper {

    RestaurantMapper INSTANCE = Mappers.getMapper(RestaurantMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", source = "addresses")
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "userOwnerId", source = "ownerId")
    Restaurant toRestaurant(CreateRestaurantRequest createRestaurantRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "userOwnerId", ignore = true)
    Restaurant toRestaurant(UpdateRestaurantRequest updateRestaurantRequest);

    @Mapping(source = "address", target = "addresses", qualifiedByName = "addressToAddressResponseList")
    @Mapping(source = "active", target = "isActive")
    RestaurantResponse toRestaurantResponse(Restaurant restaurant);

    @Mapping(source = "address", target = "addresses", qualifiedByName = "addressToAddressResponseList")
    @Mapping(source = "active", target = "isActive")
    @Mapping(target = "owner", ignore = true)
    @Named("toRestaurantResponseWithoutOwner")
    RestaurantResponse toRestaurantResponseWithoutOwner(Restaurant restaurant);

    @Mapping(target = ".", qualifiedByName = "toRestaurantResponseWithoutOwner")
    List<RestaurantResponse> toRestaurantResponseListWithoutOwner(List<Restaurant> restaurants);

    List<RestaurantResponse> toRestaurantResponseList(List<Restaurant> restaurants);

    @Named("addressToAddressResponseList")
    default List<com.fiap.foodfiapp.model.AddressesResponse> addressToAddressesResponseList(Addresses addresses) {
        if (addresses == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(AddressesMapper.INSTANCE.toAddressesResponse(addresses));
    }
}