package com.fiap.foodfiapp.core.domain.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

public class UserType extends BaseEntity {

    private UUID uuid;
    private String name;
    private boolean active;

    public UserType() {
    }

    public UserType(UUID id, String name) {
        this.uuid = id;
        this.name = name;
    }

    public UserType(UUID id, String name, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.uuid = id;
        this.name = name;
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
    }

    public UserType(UUID id, String name, boolean active, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.uuid = id;
        this.name = name;
        this.active = active;
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
