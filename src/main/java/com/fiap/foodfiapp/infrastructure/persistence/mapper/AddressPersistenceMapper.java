package com.fiap.foodfiapp.infrastructure.persistence.mapper;

import com.fiap.foodfiapp.core.domain.entity.Address;
import com.fiap.foodfiapp.infrastructure.persistence.entity.AddressEntity;

public class AddressPersistenceMapper {

    public static Address toDomain(AddressEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Address(
                entity.getId(),
                entity.getPublicPlace(),
                entity.getNumber(),
                entity.getComplement(),
                entity.getNeighborhood(),
                entity.getCity(),
                entity.getState(),
                entity.getPostalCode()
        );
    }

    public static AddressEntity toEntity(Address domain) {
        if (domain == null) {
            return null;
        }
        return AddressEntity.builder()
                .id(domain.getId())
                .publicPlace(domain.getPublicPlace())
                .number(domain.getNumber())
                .complement(domain.getComplement())
                .neighborhood(domain.getNeighborhood())
                .city(domain.getCity())
                .state(domain.getState())
                .postalCode(domain.getPostalCode())
                .build();
    }
}