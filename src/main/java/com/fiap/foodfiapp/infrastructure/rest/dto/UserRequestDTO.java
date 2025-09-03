package com.fiap.foodfiapp.infrastructure.rest.dto;

import java.util.List;

public class UserRequestDTO {
    private String name;
    private String email;
    private String cpf;
    private String login;
    private String password;
    private List<AddressRequestDTO> addresses;
    private String role;
    private boolean active;

    public UserRequestDTO() {
    }

    public UserRequestDTO(String name, String email, String cpf, String login, String password, List<AddressRequestDTO> addresses, String role, boolean active) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.login = login;
        this.password = password;
        this.addresses = addresses;
        this.role = role;
        this.active = active;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<AddressRequestDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressRequestDTO> addresses) {
        this.addresses = addresses;
    }
}

