package com.fiap.foodfiapp.core.domain.entity;

import java.util.List;
import java.util.UUID;

public class Restaurant extends BaseEntity {
    private UUID id;
    private String name;
    private String cuisineType;
    private String openingHours;
    private UUID userOwnerId;
    // private Boolean active; // REMOVA ESTA LINHA
    private String description; // ADICIONE ESTA LINHA
    private Addresses address;
    private List<MenuItem> menuItems;

    public Restaurant() {
    }

    // Construtor atualizado para incluir a descrição
    public Restaurant(UUID id, String name, String cuisineType, String openingHours,
                      UUID userOwnerId, String description, Addresses address) {
        this.id = id;
        this.name = name;
        this.cuisineType = cuisineType;
        this.openingHours = openingHours;
        this.userOwnerId = userOwnerId;
        this.description = description; // ADICIONE ESTA LINHA
        this.address = address;
    }

    // Getters e Setters

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Addresses getAddress() {
        return address;
    }

    public void setAddress(Addresses address) {
        this.address = address;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}