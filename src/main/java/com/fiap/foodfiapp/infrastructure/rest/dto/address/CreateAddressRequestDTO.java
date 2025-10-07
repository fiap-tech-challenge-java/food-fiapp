package com.fiap.foodfiapp.infrastructure.rest.dto.address;

import jakarta.validation.constraints.NotBlank;

public record CreateAddressRequestDTO(
        @NotBlank(message = "O logradouro é obrigatório.")
        String publicPlace,

        @NotBlank(message = "O número é obrigatório.")
        String number,

        String complement,

        @NotBlank(message = "O bairro é obrigatório.")
        String neighborhood,

        @NotBlank(message = "A cidade é obrigatória.")
        String city,

        @NotBlank(message = "O estado é obrigatório.")
        String state,

        @NotBlank(message = "O CEP é obrigatório.")
        String postalCode
) {}