// src/main/java/com/fiap/foodfiapp/core/application/usecases/user/DeleteUserUseCase.java
package com.fiap.foodfiapp.core.application.usecases.user;

import java.util.UUID;

public interface DeleteUserUseCase {
    void execute(UUID id);
}