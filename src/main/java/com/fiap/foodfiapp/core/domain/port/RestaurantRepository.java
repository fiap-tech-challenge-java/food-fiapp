package com.fiap.foodfiapp.core.domain.port;

import java.util.UUID;

public interface RestaurantRepository {
    boolean existsById(UUID id);
}
