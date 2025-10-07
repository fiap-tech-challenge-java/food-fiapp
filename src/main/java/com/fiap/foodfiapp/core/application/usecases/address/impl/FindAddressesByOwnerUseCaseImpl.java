package com.fiap.foodfiapp.core.application.usecases.address.impl;

import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.application.usecases.address.FindAddressesByOwnerUseCase;
import com.fiap.foodfiapp.core.domain.entity.Address;
import java.util.List;
import java.util.UUID;

public class FindAddressesByOwnerUseCaseImpl implements FindAddressesByOwnerUseCase {

    private final AddressRepository addressRepository;

    public FindAddressesByOwnerUseCaseImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Address> execute(UUID ownerId, String ownerType) {
        return addressRepository.findByOwner(ownerId, ownerType);
    }
}