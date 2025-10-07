package com.fiap.foodfiapp.core.application.usecases.address.impl;

import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.application.usecases.address.UpdateAddressUseCase;
import com.fiap.foodfiapp.core.domain.entity.Address;
import com.fiap.foodfiapp.core.domain.exception.AddressNotFoundException;
import java.util.UUID;

public class UpdateAddressUseCaseImpl implements UpdateAddressUseCase {

    private final AddressRepository addressRepository;

    public UpdateAddressUseCaseImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address execute(UUID addressId, Address addressUpdates, UUID ownerId, String ownerType) {
        // 1. Garante que o endereço existe antes de tentar atualizar.
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found."));

        // 2. Garante que o ID do endereço a ser atualizado seja o correto.
        addressUpdates.setId(existingAddress.getId());

        // 3. Persiste as alterações usando o método save polimórfico.
        return addressRepository.save(addressUpdates);
    }
}