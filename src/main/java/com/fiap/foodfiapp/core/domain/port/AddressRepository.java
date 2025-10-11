package com.fiap.foodfiapp.core.domain.port;

import com.fiap.foodfiapp.core.domain.entity.Addresses;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository {
    Optional<Addresses> findById(UUID id);

    Addresses save(Addresses addresses, UUID ownerId, String ownerType);

    void delete(UUID id);

    List<Addresses> findByOwner(UUID ownerId, String ownerType);

    Optional<Addresses> findByIdAndOwnerId(UUID id, UUID ownerId);
}