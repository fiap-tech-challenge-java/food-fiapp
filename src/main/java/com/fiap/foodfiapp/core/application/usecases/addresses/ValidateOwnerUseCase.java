package com.fiap.foodfiapp.core.application.usecases.addresses;

import java.util.UUID;

public interface ValidateOwnerUseCase {
    void execute(UUID ownerId, String ownerType);
}
