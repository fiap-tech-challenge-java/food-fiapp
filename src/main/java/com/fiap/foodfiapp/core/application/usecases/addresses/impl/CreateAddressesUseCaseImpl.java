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
        // Validate owner before creating the address
        validateOwnerUseCase.execute(ownerId, ownerType);

        // Validações de negócio podem ser adicionadas aqui.
        // Por exemplo, verificar se o ownerId realmente existe antes de associar.
        return addressRepository.save(addresses, ownerId, ownerType);
    }
}