package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.domain.entity.MenuItem;

import java.util.List;
import java.util.UUID;

public interface FindAllMenuItemsUseCase {
    List<MenuItem> execute(UUID restaurantId);
}
