package com.fiap.foodfiapp.infrastructure.rest.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private String name;
    private String email;
    private String cpf;
    private String login;
    private String password;
    private List<AddressRequestDTO> addresses;
    private UUID userTypeUuid;
    private boolean active;

}

