package com.fiap.foodfiapp.core.application.usecases.user;

import java.util.UUID;

public interface IsAdminUseCase {
    boolean execute(UUID userId);
}

