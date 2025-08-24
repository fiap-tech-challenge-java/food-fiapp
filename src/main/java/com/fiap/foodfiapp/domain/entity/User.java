package com.fiap.foodfiapp.domain.entity;

public record User(Long id, String name, String email, String password) {
    public String getEmail() {
        return email;
    }
}
