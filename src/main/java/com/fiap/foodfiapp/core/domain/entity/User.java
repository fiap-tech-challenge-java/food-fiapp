package com.fiap.foodfiapp.core.domain.entity;

import java.util.List;
import java.time.OffsetDateTime;
import java.util.UUID;

public class User {

    private UUID id;
    private String name;
    private String email;
    private String cpf;
    private String login;
    private List<Address> address;
    private OffsetDateTime updatedAt;
    private OffsetDateTime createdAt;
    private UserType userType;
    private boolean active;
    private String password;

    public User() {
    }

    public User(UUID id, String name, String email, String login, String cpf, List<Address> address, UserType userType,
                boolean active, OffsetDateTime updatedAt, OffsetDateTime createdAt, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.login = login;
        this.address = address;
        this.active = active;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.userType = userType;
        this.password = password;
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

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
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

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
