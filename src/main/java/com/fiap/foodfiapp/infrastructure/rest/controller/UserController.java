package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.application.usecases.user.CreateUserUseCase;
import com.fiap.foodfiapp.infrastructure.rest.dto.UserRequestDTO;
import com.fiap.foodfiapp.infrastructure.rest.dto.UserResponseDTO;
import com.fiap.foodfiapp.domain.entity.User;
import com.fiap.foodfiapp.domain.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final CreateUserUseCase createUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            User user = new User(null, userRequestDTO.getName(), userRequestDTO.getEmail(), userRequestDTO.getPassword());
            User createdUser = createUserUseCase.execute(user);
            UserResponseDTO responseDTO = new UserResponseDTO(createdUser.id(), createdUser.name(), createdUser.email());
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (BusinessException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}

