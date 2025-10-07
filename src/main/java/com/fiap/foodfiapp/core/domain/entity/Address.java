package com.fiap.foodfiapp.core.domain.entity;

import com.fiap.foodfiapp.core.domain.exception.InvalidDataException;
import java.util.UUID;

public class Address extends BaseEntity {

    private UUID id;
    private String publicPlace;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String postalCode;

    // Construtor para uso do framework de persistência
    public Address() {
    }

    // Construtor principal para criar a entidade
    public Address(UUID id, String publicPlace, String number, String complement, String neighborhood, String city, String state, String postalCode) {
        this.id = id;
        this.publicPlace = publicPlace;
        this.number = number;
        this.complement = complement;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        validate(); // Garante que a entidade é sempre válida ao ser criada
    }

    /**
     * Valida as regras de negócio da entidade Endereço.
     * Garante que os campos essenciais não são nulos ou vazios.
     */
    public void validate() {
        if (publicPlace == null || publicPlace.isBlank()) {
            throw new InvalidDataException("Public place (street, avenue, etc.) is mandatory.");
        }
        if (number == null || number.isBlank()) {
            throw new InvalidDataException("Number is mandatory.");
        }
        if (neighborhood == null || neighborhood.isBlank()) {
            throw new InvalidDataException("Neighborhood is mandatory.");
        }
        if (city == null || city.isBlank()) {
            throw new InvalidDataException("City is mandatory.");
        }
        if (state == null || state.isBlank()) {
            throw new InvalidDataException("State is mandatory.");
        }
        if (postalCode == null || postalCode.isBlank()) {
            throw new InvalidDataException("Postal code is mandatory.");
        }
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPublicPlace() {
        return publicPlace;
    }

    public void setPublicPlace(String publicPlace) {
        this.publicPlace = publicPlace;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}