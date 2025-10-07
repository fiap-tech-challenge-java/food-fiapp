package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.application.dto.FileUploadRequest;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;

import java.io.IOException;
import java.util.UUID;

public interface UpdateMenuItemUseCase {
    MenuItem execute(UUID authenticatedUserId, UUID id, MenuItem menuItemUpdates, FileUploadRequest photo) throws IOException;
}