package com.fiap.foodfiapp.core.domain.entities.restaurant;

import java.util.UUID;

public class UpdateRestaurant {
    private UUID id;
    private String name;
    private String cuisineType;
    private String openingHours;
    private UUID userOwnerId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public UUID getUserOwnerId() {
        return userOwnerId;
    }

    public void setUserOwnerId(UUID userOwnerId) {
        this.userOwnerId = userOwnerId;
    }
}
