package com.fiap.foodfiapp.core.application.usecases.addresses.impl;

import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.application.usecases.addresses.CreateAddressesUseCase;
import com.fiap.foodfiapp.core.application.usecases.addresses.ValidateOwnerUseCase;

import java.util.UUID;

public class CreateAddressesUseCaseImpl implements CreateAddressesUseCase {

    private final AddressRepository addressRepository;
    private final ValidateOwnerUseCase validateOwnerUseCase;

    public CreateAddressesUseCaseImpl(AddressRepository addressRepository, ValidateOwnerUseCase validateOwnerUseCase) {
        this.addressRepository = addressRepository;
        this.validateOwnerUseCase = validateOwnerUseCase;
    }

    @Override
    public Addresses execute(Addresses addresses, UUID ownerId, String ownerType) {
        if (addresses == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        if (ownerId == null) {
            throw new IllegalArgumentException("Owner ID cannot be null");
        }
        if (ownerType == null || ownerType.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner type cannot be null or empty");
        }
        
        validateOwnerUseCase.execute(ownerId, ownerType);

        return addressRepository.save(addresses, ownerId, ownerType);
    }
}