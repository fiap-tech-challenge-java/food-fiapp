package com.fiap.foodfiapp.core.domain.port;

import com.fiap.foodfiapp.core.domain.entity.Address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository {
    Optional<Address> findById(UUID id);

    // Assinatura corrigida para incluir o contexto do "dono"
    Address save(Address address, UUID ownerId, String ownerType);

    void delete(UUID id);

    List<Address> findByOwner(UUID ownerId, String ownerType);
}