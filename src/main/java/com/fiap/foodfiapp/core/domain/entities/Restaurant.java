package com.fiap.foodfiapp.core.domain.entities;

import com.fiap.foodfiapp.infrastructure.persistence.entity.AddressesEntity;

import java.util.UUID;

public class Restaurant {

    private UUID id;
    private String name;
    private String cuisineType;
    private String openingHours;
    private UUID userId;
    private AddressesEntity address;

    public Restaurant() {
    }

    public Restaurant(UUID id, String name, String cuisineType, String openingHours,
                      UUID userId, AddressesEntity address) {
        this.id = id;
        this.name = name;
        this.cuisineType = cuisineType;
        this.openingHours = openingHours;
        this.userId = userId;
        this.address = address;
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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public AddressesEntity getAddress() {
        return address;
    }

    public void setAddress(AddressesEntity address) {
        this.address = address;
    }
}
