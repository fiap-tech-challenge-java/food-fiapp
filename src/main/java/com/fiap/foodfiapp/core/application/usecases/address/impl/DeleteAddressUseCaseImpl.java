package com.fiap.foodfiapp.core.application.usecases.address.impl;

import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.application.usecases.address.DeleteAddressUseCase;
import com.fiap.foodfiapp.core.domain.exception.AddressNotFoundException;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;

import java.util.UUID;

public class DeleteAddressUseCaseImpl implements DeleteAddressUseCase {

    private final AddressRepository addressRepository;

    public DeleteAddressUseCaseImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public void execute(UUID ownerId, UUID addressId, String ownerType) {
        // Verifica existência do endereço
        var addressOpt = addressRepository.findById(addressId);
        if (addressOpt.isEmpty()) {
            throw new AddressNotFoundException("Address not found.");
        }

        // RN16: impedir exclusão quando for o último endereço ativo
        long activeCount = addressRepository.findByOwner(ownerId, ownerType)
                .stream()
                .filter(a -> Boolean.TRUE.equals(a.getIsActive()))
                .count();
        if (activeCount <= 1) {
            throw new BusinessException("Não é possível remover o último endereço de um utilizador.");
        }

        // Prossegue com a exclusão
        addressRepository.delete(addressId);
    }
}