package com.fiap.foodfiapp.core.domain.entity;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class User extends BaseEntity {

    private UUID id;
    private String name;
    private String email;
    private String cpf;
    private String login;
    private UserType userType;
    private String password;
    private List<Addresses> addresses;

    public User() {
    }

    // Construtor completo
    public User(UUID id, String name, String email, String login, String cpf, List<Addresses> addresses, UserType userType,
                Boolean isActive, OffsetDateTime updatedAt, OffsetDateTime createdAt, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.login = login;
        this.addresses = addresses;
        this.userType = userType;
        this.setIsActive(isActive);
        this.setUpdatedAt(updatedAt);
        this.setCreatedAt(createdAt);
        this.password = password;
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public List<Addresses> getAddress() { return addresses; }
    public void setAddress(List<Addresses> addresses) { this.addresses = addresses; }
}