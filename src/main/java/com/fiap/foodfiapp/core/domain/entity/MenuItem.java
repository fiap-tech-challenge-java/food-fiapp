package com.fiap.foodfiapp.core.domain.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class MenuItem extends BaseEntity {

    private UUID id;
    private String name;
    private String description;
    private Double price;
    private boolean localOnly;
    private String photoUrl;
    private UUID restaurantId;

    // Construtor principal
    public MenuItem(UUID id, String name, String description, Double price, boolean localOnly, String photoUrl, UUID restaurantId, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.localOnly = localOnly;
        this.photoUrl = photoUrl;
        this.restaurantId = restaurantId;
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
    }

    // Getters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
    public boolean isLocalOnly() { return localOnly; }
    public String getPhotoUrl() { return photoUrl; }
    public UUID getRestaurantId() { return restaurantId; }

    // Setters
    public void setId(UUID id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(Double price) { this.price = price; }
    public void setLocalOnly(boolean localOnly) { this.localOnly = localOnly; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public void setRestaurantId(UUID restaurantId) { this.restaurantId = restaurantId; }
}