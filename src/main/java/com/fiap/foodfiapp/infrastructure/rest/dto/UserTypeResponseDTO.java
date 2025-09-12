package com.fiap.foodfiapp.infrastructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTypeResponseDTO {
    private UUID uuid;
    private String name;
}
