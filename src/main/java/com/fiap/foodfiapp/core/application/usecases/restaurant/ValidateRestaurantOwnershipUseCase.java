// src/main/java/com/fiap/foodfiapp/core/application/usecases/restaurant/ValidateRestaurantOwnershipUseCase.java
package com.fiap.foodfiapp.core.application.usecases.restaurant;

import java.util.UUID;

public interface ValidateRestaurantOwnershipUseCase {
    boolean execute(UUID userId, UUID restaurantId);
}
