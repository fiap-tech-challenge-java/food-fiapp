package com.fiap.foodfiapp.core.domain.port;

import com.fiap.foodfiapp.core.domain.entity.Address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository {
    Optional<Address> findById(UUID id);

    Address save(Address address);

    void delete(UUID id);

    List<Address> findByOwner(UUID ownerId, String ownerType);
}
