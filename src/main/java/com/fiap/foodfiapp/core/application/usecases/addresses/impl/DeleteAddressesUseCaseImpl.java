package com.fiap.foodfiapp.core.application.usecases.addresses.impl;

import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.application.usecases.addresses.DeleteAddressesUseCase;
import com.fiap.foodfiapp.core.application.usecases.addresses.ValidateOwnerUseCase;
import com.fiap.foodfiapp.core.domain.exception.AddressNotFoundException;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;

import java.util.UUID;

public class DeleteAddressesUseCaseImpl implements DeleteAddressesUseCase {

    private final AddressRepository addressRepository;
    private final ValidateOwnerUseCase validateOwnerUseCase;

    public DeleteAddressesUseCaseImpl(AddressRepository addressRepository, ValidateOwnerUseCase validateOwnerUseCase) {
        this.addressRepository = addressRepository;
        this.validateOwnerUseCase = validateOwnerUseCase;
    }

    @Override
    public void execute(UUID ownerId, UUID addressId, String ownerType) {
        // Validate owner before deleting the address
        validateOwnerUseCase.execute(ownerId, ownerType);

        // Verifica existência do endereço e se pertence ao owner
        var addressOpt = addressRepository.findByIdAndOwnerId(addressId, ownerId);
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