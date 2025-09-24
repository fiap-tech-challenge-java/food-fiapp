package com.fiap.foodfiapp.core.domain.entity;

import java.util.UUID;

public class UserType {

    private UUID uuid;
    private String name;

    public UserType() {
    }

    public UserType(UUID id, String name) {
        this.uuid = id;
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
