package com.fiap.foodfiapp.core.domain.entity;

import java.util.UUID;

public class UserType {

    private final UUID id;
    private final String name;

    public UserType(UUID id, String name) {
        this.id = id;
        this.name = name;
    }


    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
