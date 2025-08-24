package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.application.usecases.user.CreateUserUseCase;
import com.fiap.foodfiapp.domain.entity.User;
import com.fiap.foodfiapp.domain.exception.BusinessException;
import com.fiap.foodfiapp.infrastructure.rest.dto.UserRequestDTO;
import com.fiap.foodfiapp.infrastructure.rest.dto.UserResponseDTO;
import com.fiap.foodfiapp.application.gateways.UserRepositoryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final CreateUserUseCase createUserUseCase;
    private final UserRepositoryGateway userRepositoryGateway;

    public UserController(CreateUserUseCase createUserUseCase, UserRepositoryGateway userRepositoryGateway) {
        this.createUserUseCase = createUserUseCase;
        this.userRepositoryGateway = userRepositoryGateway;
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

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        List<User> users = userRepositoryGateway.findAll();
        List<UserResponseDTO> response = users.stream()
                .map(u -> new UserResponseDTO(u.id(), u.name(), u.email()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}

