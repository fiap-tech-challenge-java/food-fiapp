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
    public Address execute(UUID addressId, Address addressUpdates) {
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("Address not found."));

        // A lógica de save no gateway polimórfico precisa ser ajustada para suportar atualização
        // ou você pode criar um método update específico. Por simplicidade, vamos usar o save.
        addressUpdates.setId(existingAddress.getId());

        // Para uma atualização real, você precisaria do ownerId e ownerType.
        // Esta parte precisará ser ajustada no Controller, que terá esse contexto.
        // Por enquanto, o UseCase foca na lógica de negócio principal.

        // NOTE: A chamada ao save aqui é um placeholder. A lógica de atualização
        // no gateway polimórfico precisará de mais contexto (ownerId, ownerType).
        // Vamos ajustar isso quando chegarmos nos controllers.

        // Exemplo de como seria com o save polimórfico (requer ownerId e ownerType):
        // return addressRepositoryGateway.save(addressUpdates, ownerId, ownerType);

        // Por enquanto, vamos retornar o objeto atualizado para manter o fluxo.
        return addressUpdates; // Placeholder
    }
}