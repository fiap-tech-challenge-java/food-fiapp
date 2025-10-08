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

@Mapper(uses = {UserMapper.class, AddressesMapper.class, MenuItemMapper.class}, componentModel = "spring")
public abstract class RestaurantMapper {

    @Autowired
    protected UserRepository userRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", source = "addresses")
    @Mapping(target = "userOwnerId", ignore = true)
    @Mapping(target = "description", ignore = true) // Ignoramos na criação, pode ser adicionado se necessário
    public abstract Restaurant toRestaurant(CreateRestaurantRequest createRestaurantRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", source = "addresses") // ALTERE ESTA LINHA (de ignore=true para source="addresses")
    @Mapping(target = "userOwnerId", ignore = true)
    @Mapping(target = "description", ignore = true)
    public abstract Restaurant toRestaurant(UpdateRestaurantRequest updateRestaurantRequest);


    @Mapping(source = "address", target = "addresses", qualifiedByName = "addressToAddressResponseList")
    @Mapping(source = "isActive", target = "isActive") // CORRIGIDO de "active" para "isActive"
    @Mapping(source = "userOwnerId", target = "owner", qualifiedByName = "mapOwner")
    @Mapping(source = "menuItems", target = "menuItems")
    public abstract RestaurantResponse toRestaurantResponse(Restaurant restaurant);

    public abstract List<RestaurantResponse> toRestaurantResponseList(List<Restaurant> restaurants);

    @Named("addressToAddressResponseList")
    public List<com.fiap.foodfiapp.model.AddressesResponse> addressToAddressesResponseList(Addresses address) {
        if (address == null) {
            return Collections.emptyList();
        }
        // Usa o mapper injetado para converter o objeto de endereço único em uma lista de um elemento
        return Collections.singletonList(AddressesMapper.INSTANCE.toAddressesResponse(address));
    }

    @Named("mapOwner")
    public RestaurantResponseOwner mapOwner(UUID userOwnerId) {
        if (userOwnerId == null) {
            return null;
        }

        // Busca o usuário. Se não encontrar, lança uma exceção clara.
        User user = userRepository.findById(userOwnerId)
                .orElseThrow(() -> new UserNotFoundException("Owner (user) not found with id: " + userOwnerId));

        RestaurantResponseOwner owner = new RestaurantResponseOwner();
        owner.setId(user.getId());
        owner.setName(user.getName());
        owner.setEmail(user.getEmail());
        return owner;
    }
}