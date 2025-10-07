package com.fiap.foodfiapp.core.application.usecases.menuitem.impl;

import com.fiap.foodfiapp.core.application.usecases.menuitem.FindMenuItemByIdUseCase;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindMenuItemByIdUseCaseImpl implements FindMenuItemByIdUseCase {
    private final MenuItemRepository menuItemRepository;

    @Transactional(readOnly = true)
    public Optional<MenuItem> execute(UUID id) {
        return menuItemRepository.findById(id);
    }
}
