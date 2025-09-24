package com.fiap.foodfiapp.core.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record MenuItem(
    UUID id,
    String name,
    String description,
    BigDecimal price,
    boolean localOnly,
    String photoUrl,
    UUID restaurantId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public MenuItem withPhotoUrl(String photoUrl) {
        return new MenuItem(id, name, description, price, localOnly, photoUrl, restaurantId, createdAt, updatedAt);
    }
}
