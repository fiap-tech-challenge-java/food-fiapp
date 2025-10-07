package com.fiap.foodfiapp.core.application.usecases.address.impl;

import com.fiap.foodfiapp.core.domain.port.AddressRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.address.DeleteAddressUseCase;
import com.fiap.foodfiapp.core.domain.exception.AddressNotFoundException;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class DeleteAddressUseCaseImpl implements DeleteAddressUseCase {

    private final AddressRepositoryGateway addressRepositoryGateway;

    public DeleteAddressUseCaseImpl(AddressRepositoryGateway addressRepositoryGateway) {
        this.addressRepositoryGateway = addressRepositoryGateway;
    }

    @Override
    public void execute(UUID addressId) {
        if (!addressRepositoryGateway.findById(addressId).isPresent()) {
            throw new AddressNotFoundException("Address not found.");
        }
        addressRepositoryGateway.delete(addressId);
    }
}