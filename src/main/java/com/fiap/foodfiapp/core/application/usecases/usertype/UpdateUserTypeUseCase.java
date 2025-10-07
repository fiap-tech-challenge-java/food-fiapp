package com.fiap.foodfiapp.core.application.usecases.usertype;

import com.fiap.foodfiapp.core.domain.entity.UserType;
import java.util.UUID;

public interface UpdateUserTypeUseCase {
    UserType execute(UUID uuid, UserType userTypeUpdates);
}