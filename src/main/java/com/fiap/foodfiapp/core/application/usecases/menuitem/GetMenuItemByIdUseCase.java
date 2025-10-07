package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.domain.entity.MenuItem;

import java.util.Optional;
import java.util.UUID;

public interface GetMenuItemByIdUseCase {
    Optional<MenuItem> execute(UUID id);
}
