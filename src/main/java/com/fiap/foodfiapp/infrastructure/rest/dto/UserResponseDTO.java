package com.fiap.foodfiapp.infrastructure.rest.dto;

import java.util.List;
import java.util.UUID;

public class UserResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private String cpf;
    private String login;
    private List<AddressResponseDTO> addresses;
    private String role;
    private boolean active;

    public UserResponseDTO() {}

    public UserResponseDTO(UUID id, String name, String email, String cpf, String login, List<AddressResponseDTO> addresses, String role, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.login = login;
        this.addresses = addresses;
        this.role = role;
        this.active = active;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

