package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetMenuItemsByRestaurantUseCase {
    private final MenuItemRepository menuItemRepository;

    public List<MenuItem> execute(UUID restaurantId) {
        return menuItemRepository.findAllByRestaurantId(restaurantId);
    }
}
