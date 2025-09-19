package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import com.fiap.foodfiapp.core.domain.port.MenuItemRepository;
import com.fiap.foodfiapp.core.domain.port.RestaurantRepository;
import com.fiap.foodfiapp.core.domain.port.FileStorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateMenuItemUseCase {
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final FileStorageRepository fileStorageRepository;

    @Transactional
    public MenuItem execute(MenuItem menuItem, MultipartFile photo) throws IOException {
        if (!restaurantRepository.existsById(menuItem.restaurantId())) {
            throw new IllegalArgumentException("Restaurant not found");
        }

        String photoUrl = null;
        if (photo != null && !photo.isEmpty()) {
            String fileName = String.format("%s-%s-%s", menuItem.restaurantId(), UUID.randomUUID(), photo.getOriginalFilename());
            photoUrl = fileStorageRepository.store(photo, fileName);
        }

        var itemToSave = menuItem.withPhotoUrl(photoUrl);
        return menuItemRepository.save(itemToSave);
    }
}
