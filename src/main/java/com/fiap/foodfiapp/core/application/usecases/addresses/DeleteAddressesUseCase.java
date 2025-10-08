package com.fiap.foodfiapp.core.application.usecases.addresses;

import java.util.UUID;

public interface DeleteAddressesUseCase {
    void execute(UUID ownerId, UUID addressId, String ownerType);
}