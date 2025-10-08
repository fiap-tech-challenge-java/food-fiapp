package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.model.CreateRestaurantRequest;
import com.fiap.foodfiapp.model.RestaurantResponse;
import com.fiap.foodfiapp.model.RestaurantResponseOwner;
import com.fiap.foodfiapp.model.UpdateRestaurantRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Collections;
import java.util.Optional;

@Mapper(uses = {UserMapper.class, AddressesMapper.class}, componentModel = "spring")
public abstract class RestaurantMapper {

    @Autowired
    protected UserRepository userRepository;

    public static final RestaurantMapper INSTANCE = Mappers.getMapper(RestaurantMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", source = "addresses")
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "userOwnerId", ignore = true)
    public abstract Restaurant toRestaurant(CreateRestaurantRequest createRestaurantRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "userOwnerId", ignore = true)
    public abstract Restaurant toRestaurant(UpdateRestaurantRequest updateRestaurantRequest);

    @Mapping(source = "address", target = "addresses", qualifiedByName = "addressToAddressResponseList")
    @Mapping(source = "active", target = "isActive")
    @Mapping(source = "userOwnerId", target = "owner", qualifiedByName = "mapOwner")
    public abstract RestaurantResponse toRestaurantResponse(Restaurant restaurant);

    @Mapping(source = "address", target = "addresses", qualifiedByName = "addressToAddressResponseList")
    @Mapping(source = "active", target = "isActive")
    @Mapping(target = "owner", ignore = true)
    @Named("toRestaurantResponseWithoutOwner")
    public abstract RestaurantResponse toRestaurantResponseWithoutOwner(Restaurant restaurant);

    public abstract List<RestaurantResponse> toRestaurantResponseList(List<Restaurant> restaurants);

    @Named("addressToAddressResponseList")
    public List<com.fiap.foodfiapp.model.AddressesResponse> addressToAddressesResponseList(Addresses addresses) {
        if (addresses == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(AddressesMapper.INSTANCE.toAddressesResponse(addresses));
    }

    @Named("mapOwner")
    public RestaurantResponseOwner mapOwner(java.util.UUID userOwnerId) {
        if (userOwnerId == null || userRepository == null) {
            return null;
        }

        try {
            Optional<User> userOptional = userRepository.findById(userOwnerId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                RestaurantResponseOwner owner = new RestaurantResponseOwner();
                owner.setId(user.getId());
                owner.setName(user.getName());
                owner.setEmail(user.getEmail());
                return owner;
            }
        } catch (Exception e) {
            // Log the error if needed, but don't break the mapping
            System.err.println("Error mapping owner: " + e.getMessage());
        }

        return null;
    }
}