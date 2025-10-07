package com.fiap.foodfiapp.core.application.usecases.address.impl;

import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.application.usecases.address.DeleteAddressUseCase;
import com.fiap.foodfiapp.core.domain.exception.AddressNotFoundException;
import java.util.UUID;

public class DeleteAddressUseCaseImpl implements DeleteAddressUseCase {

    private final AddressRepository addressRepository;

    public DeleteAddressUseCaseImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public void execute(UUID addressId) {
        if (!addressRepository.findById(addressId).isPresent()) {
            throw new AddressNotFoundException("Address not found.");
        }
        addressRepository.delete(addressId);
    }
}