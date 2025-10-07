package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.infrastructure.persistence.mapper.MenuItemPersistenceMapper;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.MenuItemSpringDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MenuItemRepositoryImpl implements MenuItemRepository {

    private final MenuItemSpringDataRepository menuItemSpringDataRepository;
    private final MenuItemPersistenceMapper menuItemMapper = MenuItemPersistenceMapper.INSTANCE;

    @Override
    public MenuItem save(MenuItem menuItem) {
        var entity = menuItemMapper.toEntity(menuItem);
        var savedEntity = menuItemSpringDataRepository.save(entity);
        return menuItemMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<MenuItem> findById(UUID id) {
        return menuItemSpringDataRepository.findById(id)
                .map(menuItemMapper::toDomain);
    }

    @Override
    public List<MenuItem> findAllByRestaurantId(UUID restaurantId) {
        var entities = menuItemSpringDataRepository.findAllByRestaurantId(restaurantId);
        return menuItemMapper.toDomainList(entities);
    }

    @Override
    public void deleteById(UUID id) {
        menuItemSpringDataRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return menuItemSpringDataRepository.existsById(id);
    }
}