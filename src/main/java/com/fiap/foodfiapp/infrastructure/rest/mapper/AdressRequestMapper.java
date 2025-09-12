package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.Address;
import com.fiap.foodfiapp.infrastructure.rest.dto.AddressRequestDTO;

import java.util.List;

public class AdressRequestMapper {
    private AdressRequestMapper(){}

    public static Address toEntity(AddressRequestDTO dto) {
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

    public static List<Address> toEntity(List<AddressRequestDTO> dto) {
        if (dto == null) {
            return null;
        }
        return dto.stream().map(AdressRequestMapper::toEntity).toList();
    }
}
