package com.fiap.foodfiapp.core.application.usecases.user;

import java.util.UUID;

public interface IsAdminUseCase {
    /**
     * Checks if a user is an ADMIN
     * @param userId The user ID to check
     * @return true if user is ADMIN, false otherwise
     */
    boolean execute(UUID userId);
}

