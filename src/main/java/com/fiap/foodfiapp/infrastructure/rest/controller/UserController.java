package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.api.UsersApi;
import com.fiap.foodfiapp.core.application.usecases.user.CreateUserUseCase;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.infrastructure.rest.mapper.CreateUserRequestMapper;
import com.fiap.foodfiapp.infrastructure.rest.mapper.UpdateUserRequestMapper;
import com.fiap.foodfiapp.infrastructure.rest.mapper.UserResponseMapper;
import com.fiap.foodfiapp.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController implements UsersApi {
    private final CreateUserUseCase createUserUseCase;
    private final UserRepositoryGateway userRepositoryGateway;

    public UserController(CreateUserUseCase createUserUseCase, UserRepositoryGateway userRepositoryGateway) {
        this.createUserUseCase = createUserUseCase;
        this.userRepositoryGateway = userRepositoryGateway;
    }

    @Override
    public ResponseEntity<UserResponse> createUser(CreateUserRequest createUserRequest) {
        try {
            var user = CreateUserRequestMapper.toEntity(createUserRequest);
            var createdUser = createUserUseCase.execute(user);
            var responseDTO = UserResponseMapper.toDTO(createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (BusinessException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userRepositoryGateway.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserResponse> getUser(UUID id) {
        return userRepositoryGateway.findById(id)
                .map(UserResponseMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<UserResponse>> getUsers() {
        var users = userRepositoryGateway.findAll();
        var response = users.stream()
                .map(UserResponseMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(UUID id, UpdateUserRequest updateUserRequest) {
        try {
            var existingUser = userRepositoryGateway.findById(id).orElse(null);
            if (existingUser == null) {
                return ResponseEntity.notFound().build();
            }
            var updatedUser = UpdateUserRequestMapper.toEntity(updateUserRequest);
            updatedUser.setId(id);
            var savedUser = userRepositoryGateway.save(updatedUser);
            var responseDTO = UserResponseMapper.toDTO(savedUser);
            return ResponseEntity.ok(responseDTO);
        } catch (BusinessException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Override
    public ResponseEntity<Void> changePassword(ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
