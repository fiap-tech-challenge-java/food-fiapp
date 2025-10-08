package com.fiap.foodfiapp.core.application.usecases.addresses.impl;

import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.application.usecases.addresses.UpdateAddressesUseCase;
import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.exception.AddressNotFoundException;
import java.util.UUID;

public class UpdateAddressesUseCaseImpl implements UpdateAddressesUseCase {

    private final AddressRepository addressRepository;

    public UpdateAddressesUseCaseImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Addresses execute(UUID addressId, Addresses addressesUpdates, UUID ownerId, String ownerType) {
        Addresses existingAddresses = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found."));

        addressesUpdates.setId(existingAddresses.getId());

        return addressRepository.save(addressesUpdates, ownerId, ownerType);
    }
}