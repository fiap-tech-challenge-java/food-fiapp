package com.fiap.foodfiapp.infrastructure.config;

import com.fiap.foodfiapp.core.application.usecases.menuitem.*;
import com.fiap.foodfiapp.core.application.usecases.menuitem.impl.*;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MenuItemUseCaseConfig {

    @Bean
    public CreateMenuItemUseCase createMenuItemUseCase(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository, FileStorageRepository fileStorageRepository) {
        return new CreateMenuItemUseCaseImpl(menuItemRepository, restaurantRepository, fileStorageRepository);
    }

    @Bean
    public DeleteMenuItemUseCase deleteMenuItemUseCase(MenuItemRepository menuItemRepository, FileStorageRepository fileStorageRepository) {
        return new DeleteMenuItemUseCaseImpl(menuItemRepository, fileStorageRepository);
    }

    @Bean
    public UpdateMenuItemUseCase updateMenuItemUseCase(MenuItemRepository menuItemRepository, FileStorageRepository fileStorageRepository) {
        return new UpdateMenuItemUseCaseImpl(menuItemRepository, fileStorageRepository);
    }

    @Bean
    public FindMenuItemByIdUseCase findMenuItemByIdUseCase(MenuItemRepository menuItemRepository) {
        return new FindMenuItemByIdUseCaseImpl(menuItemRepository);
    }

    @Bean
    public FindAllMenuItemsUseCase findAllMenuItemsUseCase(MenuItemRepository menuItemRepository) {
        return new FindAllMenuItemsUseCaseImpl(menuItemRepository);
    }
}