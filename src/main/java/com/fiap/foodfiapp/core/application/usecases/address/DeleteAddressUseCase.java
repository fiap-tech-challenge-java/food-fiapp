package com.fiap.foodfiapp.core.application.usecases.address;

import java.util.UUID;

public interface DeleteAddressUseCase {
    void execute(UUID ownerId, UUID addressId, String ownerType);
}