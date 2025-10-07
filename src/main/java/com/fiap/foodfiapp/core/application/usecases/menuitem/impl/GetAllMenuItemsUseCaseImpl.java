package com.fiap.foodfiapp.core.application.usecases.menuitem.impl;

import com.fiap.foodfiapp.core.application.usecases.menuitem.GetAllMenuItemsUseCase;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetAllMenuItemsUseCaseImpl implements GetAllMenuItemsUseCase {
    private final MenuItemRepository menuItemRepository;

    @Transactional(readOnly = true)
    public List<MenuItem> execute(UUID restaurantId) {
        return menuItemRepository.findAllByRestaurantId(restaurantId);
    }
}
