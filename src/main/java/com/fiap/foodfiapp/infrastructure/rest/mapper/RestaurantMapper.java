package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.entity.Restaurant;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.exception.UserNotFoundException;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.model.CreateRestaurantRequest;
import com.fiap.foodfiapp.model.RestaurantResponse;
import com.fiap.foodfiapp.model.RestaurantResponseOwner;
import com.fiap.foodfiapp.model.UpdateRestaurantRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Mapper(uses = { UserMapper.class, AddressesMapper.class, MenuItemMapper.class }, componentModel = "spring")
public abstract class RestaurantMapper {

    @Autowired
    protected UserRepository userRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", source = "addresses")
    @Mapping(target = "userOwnerId", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "menuItems", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract Restaurant toRestaurant(CreateRestaurantRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", source = "addresses")
    @Mapping(target = "userOwnerId", ignore = true)
    @Mapping(target = "menuItems", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract Restaurant toRestaurant(UpdateRestaurantRequest request);

    @Mapping(source = "address", target = "addresses", qualifiedByName = "addressToAddressResponseList")
    @Mapping(source = "isActive", target = "isActive")
    @Mapping(source = "userOwnerId", target = "owner", qualifiedByName = "mapOwner")
    @Mapping(source = "menuItems", target = "menuItems")
    public abstract RestaurantResponse toRestaurantResponse(Restaurant restaurant);

    public abstract List<RestaurantResponse> toRestaurantResponseList(List<Restaurant> restaurants);

    protected Addresses map(com.fiap.foodfiapp.model.AddressesRequest addressesRequest) {
        if (addressesRequest == null) {
            return null;
        }

        Addresses addresses = new Addresses();
        addresses.setPublicPlace(addressesRequest.getPublicPlace());
        addresses.setNumber(addressesRequest.getNumber());
        addresses.setComplement(addressesRequest.getComplement());
        addresses.setNeighborhood(addressesRequest.getNeighborhood());
        addresses.setCity(addressesRequest.getCity());
        addresses.setState(addressesRequest.getState());
        addresses.setPostalCode(addressesRequest.getPostalCode());
        addresses.setIsActive(true);

        return addresses;
    }

    @Named("addressToAddressResponseList")
    public List<com.fiap.foodfiapp.model.AddressesResponse> addressToAddressesResponseList(Addresses address) {
        if (address == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(AddressesMapper.INSTANCE.toAddressesResponse(address));
    }

    @Named("mapOwner")
    public RestaurantResponseOwner mapOwner(UUID userOwnerId) {
        if (userOwnerId == null) {
            return null;
        }

        User user = userRepository.findById(userOwnerId)
                .orElseThrow(() -> new UserNotFoundException("Owner (user) not found with id: " + userOwnerId));

        RestaurantResponseOwner owner = new RestaurantResponseOwner();
        owner.setId(user.getId());
        owner.setName(user.getName());
        owner.setEmail(user.getEmail());
        return owner;
    }
}