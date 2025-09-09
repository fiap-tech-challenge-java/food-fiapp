package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.Address;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.infrastructure.rest.dto.AddressResponseDTO;
import com.fiap.foodfiapp.infrastructure.rest.dto.UserResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class UserResponseMapper {
    private UserResponseMapper() {
    }

    public static UserResponseDTO toDTO(User user) {
        List<AddressResponseDTO> addresses = null;
        if (user.getAddresses() != null) {
            addresses = user.getAddresses().stream()
                    .map(UserResponseMapper::toAddressDTO)
                    .collect(Collectors.toList());
        }

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCpf(),
                user.getLogin(),
                addresses,
                user.getUserType().getName(),
                user.isActive()
        );
    }

    private static AddressResponseDTO toAddressDTO(Address address) {
        return new AddressResponseDTO(
                address.getId(),
                address.getPublicPlace(),
                address.getNumber(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getPostalCode()
        );
    }
}

