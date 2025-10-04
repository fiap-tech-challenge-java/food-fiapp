package com.fiap.foodfiapp.infrastructure.rest.mapper;

import com.fiap.foodfiapp.core.domain.entity.Address;
import com.fiap.foodfiapp.model.AddressRequest;

import java.util.List;

public class AddressRequestMapper {
    private AddressRequestMapper() {
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
                dto.getPostalCode(),
                dto.getIsActive(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }

    public static List<Address> toEntity(List<AddressRequest> dto) {
        if (dto == null) {
            return List.of();
        }
        return dto.stream().map(AddressRequestMapper::toEntity).toList();
    }
}
