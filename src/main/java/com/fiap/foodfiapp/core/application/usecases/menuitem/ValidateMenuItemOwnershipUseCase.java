package com.fiap.foodfiapp.core.application.usecases.menuitem;

import java.util.UUID;

public interface ValidateMenuItemOwnershipUseCase {
    boolean execute(UUID itemId, UUID restaurantId);
}
