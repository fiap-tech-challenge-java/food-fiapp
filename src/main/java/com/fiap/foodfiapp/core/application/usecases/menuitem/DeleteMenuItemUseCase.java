package com.fiap.foodfiapp.core.application.usecases.menuitem;

import java.util.UUID;

public interface DeleteMenuItemUseCase {
    void execute(UUID authenticatedUserId, UUID itemId);
}
