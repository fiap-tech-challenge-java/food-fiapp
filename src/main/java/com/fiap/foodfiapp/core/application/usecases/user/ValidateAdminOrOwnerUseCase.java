package com.fiap.foodfiapp.core.application.usecases.user;

import java.util.UUID;

public interface ValidateAdminOrOwnerUseCase {
    /**
     * Validates if the user is an ADMIN or if they are the owner of the resource
     * @param userId The user ID to validate
     * @param resourceOwnerId The owner ID of the resource (can be null if only checking for ADMIN)
     * @return true if user is ADMIN or is the owner, false otherwise
     */
    boolean execute(UUID userId, UUID resourceOwnerId);
}

