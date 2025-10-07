package com.fiap.foodfiapp.core.application.usecases.menuitem.impl;

import com.fiap.foodfiapp.core.application.usecases.menuitem.GetMenuItemsByRestaurantUseCase;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetMenuItemsByRestaurantUseCaseImpl implements GetMenuItemsByRestaurantUseCase {
    private final MenuItemRepository menuItemRepository;

    public List<MenuItem> execute(UUID restaurantId) {
        return menuItemRepository.findAllByRestaurantId(restaurantId);
    }
}
