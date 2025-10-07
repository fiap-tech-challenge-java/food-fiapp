package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface UpdateMenuItemUseCase {
    MenuItem execute(UUID id, MenuItem menuItemUpdates, MultipartFile photo) throws IOException;
}
