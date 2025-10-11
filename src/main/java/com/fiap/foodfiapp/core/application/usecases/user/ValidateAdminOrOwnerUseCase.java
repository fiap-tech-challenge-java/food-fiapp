package com.fiap.foodfiapp.core.application.usecases.user;

import java.util.UUID;

public interface ValidateAdminOrOwnerUseCase {
    boolean execute(UUID userId, UUID resourceOwnerId);
}

