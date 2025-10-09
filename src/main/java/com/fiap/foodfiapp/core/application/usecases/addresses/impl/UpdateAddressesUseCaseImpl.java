package com.fiap.foodfiapp.core.application.usecases.addresses.impl;

import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.application.usecases.addresses.UpdateAddressesUseCase;
import com.fiap.foodfiapp.core.application.usecases.addresses.ValidateOwnerUseCase;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.exception.AddressNotFoundException;
import java.util.UUID;

public class UpdateAddressesUseCaseImpl implements UpdateAddressesUseCase {

    private final AddressRepository addressRepository;
    private final ValidateOwnerUseCase validateOwnerUseCase;

    public UpdateAddressesUseCaseImpl(AddressRepository addressRepository, ValidateOwnerUseCase validateOwnerUseCase) {
        this.addressRepository = addressRepository;
        this.validateOwnerUseCase = validateOwnerUseCase;
    }

    @Override
    public Addresses execute(UUID addressId, Addresses addressesUpdates, UUID ownerId, String ownerType) {
        // Validate input parameters first
        if (addressesUpdates == null) {
            throw new NullPointerException("Address updates cannot be null");
        }
        if (ownerId == null) {
            throw new NullPointerException("Owner ID cannot be null");
        }
        if (ownerType == null) {
            throw new NullPointerException("Owner type cannot be null");
        }
        if (ownerType.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner type cannot be empty");
        }
        
        // Validate owner before updating the address
        validateOwnerUseCase.execute(ownerId, ownerType);

        Addresses existingAddresses = addressRepository.findByIdAndOwnerId(addressId, ownerId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found."));

        addressesUpdates.setId(existingAddresses.getId());

        return addressRepository.save(addressesUpdates, ownerId, ownerType);
    }
}