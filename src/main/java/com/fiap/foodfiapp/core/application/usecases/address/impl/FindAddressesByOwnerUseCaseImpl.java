package com.fiap.foodfiapp.core.application.usecases.address.impl;

import com.fiap.foodfiapp.core.application.gateways.AddressRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.address.FindAddressesByOwnerUseCase;
import com.fiap.foodfiapp.core.domain.entity.Address;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class FindAddressesByOwnerUseCaseImpl implements FindAddressesByOwnerUseCase {

    private final AddressRepositoryGateway addressRepositoryGateway;

    public FindAddressesByOwnerUseCaseImpl(AddressRepositoryGateway addressRepositoryGateway) {
        this.addressRepositoryGateway = addressRepositoryGateway;
    }

    @Override
    public List<Address> execute(UUID ownerId, String ownerType) {
        return addressRepositoryGateway.findByOwner(ownerId, ownerType);
    }
}