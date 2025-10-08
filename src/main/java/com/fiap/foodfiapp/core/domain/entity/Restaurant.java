package com.fiap.foodfiapp.core.domain.entity;

import java.util.UUID;

public class Restaurant extends BaseEntity {
    private UUID id;
    private String name;
    private String cuisineType;
    private String openingHours;
    private UUID userOwnerId;
    private Boolean active;
    private Addresses addresses;

    public Restaurant() {
    }

    public Restaurant(UUID id, String name, String cuisineType, String openingHours,
                      UUID userOwnerId, Boolean active, Addresses addresses) {
        this.id = id;
        this.name = name;
        this.cuisineType = cuisineType;
        this.openingHours = openingHours;
        this.userOwnerId = userOwnerId;
        this.active = active;
        this.addresses = addresses;
    }

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Addresses getAddress() {
        return addresses;
    }

    public void setAddress(Addresses addresses) {
        this.addresses = addresses;
    }
}
