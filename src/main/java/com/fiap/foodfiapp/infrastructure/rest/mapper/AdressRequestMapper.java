package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.Address;
import com.fiap.foodfiapp.model.AddressRequest;

import java.util.List;

public class AdressRequestMapper {
    private AdressRequestMapper() {
    }

    public static Address toEntity(AddressRequest dto) {
        return new Address(
                dto.getId(),
                dto.getPublicPlace(),
                dto.getNumber(),
                dto.getComplement(),
                dto.getNeighborhood(),
                dto.getCity(),
                dto.getState(),
                dto.getPostalCode()
        );
    }

    public static List<Address> toEntity(List<AddressRequest> dto) {
        return dto.stream().map(AdressRequestMapper::toEntity).toList();
    }
}
