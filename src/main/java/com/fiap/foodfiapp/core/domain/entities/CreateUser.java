package com.fiap.foodfiapp.core.domain.entities;

public class CreateUser {
    private String name;
    private String email;
    private String cpf;
    private String login;
    private String password;
    private Long userTypeId;

    public CreateUser() {
    }

    public CreateUser(String name, String email, String cpf, String login, String password, Long userTypeId) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.login = login;
        this.password = password;
        this.userTypeId = userTypeId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Long userTypeId) {
        this.userTypeId = userTypeId;
    }
}
