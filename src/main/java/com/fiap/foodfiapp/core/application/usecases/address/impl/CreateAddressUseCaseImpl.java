package com.fiap.foodfiapp.core.application.usecases.address.impl;

import com.fiap.foodfiapp.core.domain.port.AddressRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.address.CreateAddressUseCase;
import com.fiap.foodfiapp.core.domain.entity.Address;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CreateAddressUseCaseImpl implements CreateAddressUseCase {

    private final AddressRepositoryGateway addressRepositoryGateway;

    public CreateAddressUseCaseImpl(AddressRepositoryGateway addressRepositoryGateway) {
        this.addressRepositoryGateway = addressRepositoryGateway;
    }

    @Override
    public Address execute(Address address, UUID ownerId, String ownerType) {
        // Validações de negócio podem ser adicionadas aqui.
        // Por exemplo, verificar se o ownerId realmente existe antes de associar.
        return addressRepositoryGateway.save(address, ownerId, ownerType);
    }
}