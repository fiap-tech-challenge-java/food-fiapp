package com.fiap.foodfiapp.infrastructure.rest.dto.user;

import com.fiap.foodfiapp.core.domain.entities.Address;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class UserResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private String cpf;
    private String login;
    private List<Address> addresses;
    private OffsetDateTime updatedAt;
    private OffsetDateTime createdAt;
    private boolean active;

    public UserResponseDTO() {}

    public UserResponseDTO(UUID id, String name, String email, String cpf, String login,
                           List<Address> addresses, OffsetDateTime updatedAt,
                           OffsetDateTime createdAt, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.login = login;
        this.addresses = addresses;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.active = active;
    }

    public UserResponseDTO(UUID id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}