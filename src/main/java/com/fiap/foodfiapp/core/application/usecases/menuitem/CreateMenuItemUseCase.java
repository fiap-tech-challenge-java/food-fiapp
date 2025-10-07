package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.domain.entity.MenuItem;

public interface CreateMenuItemUseCase {
    MenuItem execute(MenuItem menuItem);
}
