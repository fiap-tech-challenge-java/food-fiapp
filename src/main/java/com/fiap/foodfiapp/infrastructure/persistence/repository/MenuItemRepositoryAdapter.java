package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.infrastructure.persistence.mapper.MenuItemEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MenuItemRepositoryAdapter implements MenuItemRepository {
    private final MenuItemJpaRepository menuItemJpaRepository;
    private final MenuItemEntityMapper menuItemEntityMapper;

    @Override
    public MenuItem save(MenuItem menuItem) {
        var entity = menuItemEntityMapper.toEntity(menuItem);
        var savedEntity = menuItemJpaRepository.save(entity);
        return menuItemEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<MenuItem> findById(UUID id) {
        return menuItemJpaRepository.findById(id)
                .map(menuItemEntityMapper::toDomain);
    }

    @Override
    public List<MenuItem> findAllByRestaurantId(UUID restaurantId) {
        return menuItemJpaRepository.findAllByRestaurantId(restaurantId)
                .stream()
                .map(menuItemEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        menuItemJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return menuItemJpaRepository.existsById(id);
    }
}
