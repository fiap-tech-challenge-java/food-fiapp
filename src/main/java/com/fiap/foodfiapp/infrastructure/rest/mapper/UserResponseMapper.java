package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.Address;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.model.AddressResponse;
import com.fiap.foodfiapp.model.UserResponse;

import java.util.List;
import java.util.stream.Collectors;


public class UserResponseMapper {
    private UserResponseMapper() {
    }

    public static UserResponse toDTO(User user) {
        List<AddressResponse> addresses = null;
        if (user.getAddresses() != null) {
            addresses = user.getAddresses().stream()
                    .map(UserResponseMapper::toAddressDTO)
                    .collect(Collectors.toList());
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setCpf(user.getCpf());
        userResponse.setLogin(user.getLogin());
        userResponse.setAddresses(addresses);
        userResponse.setUserType(user.getUserType() != null ? user.getUserType().getName() : null);
        userResponse.setIsActive(user.isActive());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        return userResponse;
    }

    private static AddressResponse toAddressDTO(Address address) {

        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setId(address.getId());
        addressResponse.setPublicPlace(address.getPublicPlace());
        addressResponse.setNumber(address.getNumber());
        addressResponse.setComplement(address.getComplement());
        addressResponse.setNeighborhood(address.getNeighborhood());
        addressResponse.setCity(address.getCity());
        addressResponse.setState(address.getState());
        addressResponse.setPostalCode(address.getPostalCode());
        addressResponse.setCreatedAt(address.getCreatedAt());
        addressResponse.setUpdatedAt(address.getUpdatedAt());
        addressResponse.setIsActive(address.getIsActive());

        return addressResponse;
    }
}
