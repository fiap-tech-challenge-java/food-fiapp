package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.core.application.usecases.user.CreateUserUseCase;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.infrastructure.rest.dto.UserRequestDTO;
import com.fiap.foodfiapp.infrastructure.rest.dto.UserResponseDTO;
import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.infrastructure.rest.mapper.UserRequestMapper;
import com.fiap.foodfiapp.infrastructure.rest.mapper.UserResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
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
            var user = UserRequestMapper.toEntity(userRequestDTO);
            var createdUser = createUserUseCase.execute(user);
            var responseDTO = UserResponseMapper.toDTO(createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (BusinessException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        var users = userRepositoryGateway.findAll();
        var response = users.stream()
                .map(UserResponseMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable UUID id) {
        return userRepositoryGateway.findById(id)
                .map(UserResponseMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id, @RequestBody UserRequestDTO userRequestDTO) {
        try {
            var existingUser = userRepositoryGateway.findById(id).orElse(null);
            if (existingUser == null) {
                return ResponseEntity.notFound().build();
            }
            var updatedUser = UserRequestMapper.toEntity(userRequestDTO);
            updatedUser.setId(id);
            var savedUser = userRepositoryGateway.save(updatedUser);
            var responseDTO = UserResponseMapper.toDTO(savedUser);
            return ResponseEntity.ok(responseDTO);
        } catch (BusinessException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userRepositoryGateway.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
