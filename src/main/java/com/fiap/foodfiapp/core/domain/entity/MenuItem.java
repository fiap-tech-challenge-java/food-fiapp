package com.fiap.foodfiapp.core.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class MenuItem {
    private final UUID id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final boolean localOnly;
    private final String photoUrl;
    private final UUID restaurantId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public MenuItem(UUID id,
                    String name,
                    String description,
                    BigDecimal price,
                    boolean localOnly,
                    String photoUrl,
                    UUID restaurantId,
                    LocalDateTime createdAt,
                    LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.localOnly = localOnly;
        this.photoUrl = photoUrl;
        this.restaurantId = restaurantId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Factory/helper para atualizar apenas a foto mantendo os demais campos
    public MenuItem withPhotoUrl(String photoUrl) {
        return new MenuItem(id, name, description, price, localOnly, photoUrl, restaurantId, createdAt, updatedAt);
    }

    // Métodos estilo record para compatibilidade com os use cases existentes
    public UUID id() { return id; }
    public String name() { return name; }
    public String description() { return description; }
    public BigDecimal price() { return price; }
    public boolean localOnly() { return localOnly; }
    public String photoUrl() { return photoUrl; }
    public UUID restaurantId() { return restaurantId; }
    public LocalDateTime createdAt() { return createdAt; }
    public LocalDateTime updatedAt() { return updatedAt; }
}
