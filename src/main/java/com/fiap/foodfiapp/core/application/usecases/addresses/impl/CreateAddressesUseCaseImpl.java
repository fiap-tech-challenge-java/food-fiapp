package com.fiap.foodfiapp.core.application.usecases.addresses.impl;

import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.application.usecases.addresses.CreateAddressesUseCase;

import java.util.UUID;

public class CreateAddressesUseCaseImpl implements CreateAddressesUseCase {

    private final AddressRepository addressRepository;

    public CreateAddressesUseCaseImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Addresses execute(Addresses addresses, UUID ownerId, String ownerType) {
        // Validações de negócio podem ser adicionadas aqui.
        // Por exemplo, verificar se o ownerId realmente existe antes de associar.
        return addressRepository.save(addresses, ownerId, ownerType);
    }
}