package com.fiap.foodfiapp.core.application.usecases.addresses.impl;

import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.application.usecases.addresses.FindAddressesByOwnerUseCase;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import java.util.List;
import java.util.UUID;

public class FindAddressesByOwnerUseCaseImpl implements FindAddressesByOwnerUseCase {

    private final AddressRepository addressRepository;

    public FindAddressesByOwnerUseCaseImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Addresses> execute(UUID ownerId, String ownerType) {
        if (ownerId == null) {
            throw new IllegalArgumentException("Owner ID cannot be null");
        }
        if (ownerType == null || ownerType.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner type cannot be null or empty");
        }
        
        return addressRepository.findByOwner(ownerId, ownerType);
    }
}