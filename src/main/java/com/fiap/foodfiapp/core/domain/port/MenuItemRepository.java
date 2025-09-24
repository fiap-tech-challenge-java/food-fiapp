package com.fiap.foodfiapp.core.domain.port;

import com.fiap.foodfiapp.core.domain.entity.MenuItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuItemRepository {
    MenuItem save(MenuItem menuItem);
    Optional<MenuItem> findById(UUID id);
    List<MenuItem> findAllByRestaurantId(UUID restaurantId);
    void deleteById(UUID id);
    boolean existsById(UUID id);
}
