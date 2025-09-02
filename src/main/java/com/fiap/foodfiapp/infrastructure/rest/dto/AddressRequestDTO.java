package com.fiap.foodfiapp.infrastructure.rest.dto;

import java.util.UUID;

public class AddressRequestDTO {
    private UUID id;
    private String publicPlace;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String postalCode;

    public AddressRequestDTO() {
    }

    public AddressRequestDTO(UUID id, String publicPlace, String number, String complement, String neighborhood, String city, String state, String postalCode) {
        this.id = id;
        this.publicPlace = publicPlace;
        this.number = number;
        this.complement = complement;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
