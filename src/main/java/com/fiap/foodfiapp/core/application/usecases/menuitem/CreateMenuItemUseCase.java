package com.fiap.foodfiapp.core.application.usecases.menuitem;

import com.fiap.foodfiapp.core.application.dto.FileUploadRequest;
import com.fiap.foodfiapp.core.domain.entity.MenuItem;
import java.io.IOException;

public interface CreateMenuItemUseCase {
    MenuItem execute(MenuItem menuItem, FileUploadRequest photo) throws IOException;
}