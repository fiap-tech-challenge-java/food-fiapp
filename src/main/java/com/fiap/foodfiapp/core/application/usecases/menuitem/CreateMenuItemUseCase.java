package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.application.dto.FileUploadRequest;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import java.io.IOException;
import java.util.UUID;

public interface CreateMenuItemUseCase {
    MenuItem execute(UUID restaurantId, String name, String description, Double price, Boolean availableForInStoreOnly, FileUploadRequest photo) throws IOException;
}