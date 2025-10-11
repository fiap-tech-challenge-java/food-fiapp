package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.api.UsersApi;
import com.fiap.foodfiapp.core.application.usecases.user.*;
import com.fiap.foodfiapp.infrastructure.rest.mapper.UserMapper;
import com.fiap.foodfiapp.infrastructure.security.AuthenticationService;
import com.fiap.foodfiapp.model.ChangePasswordRequest;
import com.fiap.foodfiapp.model.CreateUserRequest;
import com.fiap.foodfiapp.model.UpdateUserRequest;
import com.fiap.foodfiapp.model.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final FindUserUseCase findUserUseCase;
    private final FindAllUserUseCase findAllUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final AuthenticationService authenticationService;

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public ResponseEntity<UserResponse> createUser(CreateUserRequest createUserRequest) {
        var user = userMapper.toUser(createUserRequest);
        var createdUser = createUserUseCase.execute(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toUserResponse(createdUser));
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID id) {

        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserResponse> getUser(UUID id) {


        return findUserUseCase.execute(id)
                .map(userMapper::toUserResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<UserResponse>> getUsers() {

        var users = findAllUserUseCase.execute();
        return ResponseEntity.ok(userMapper.toUserResponseList(users));
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(UUID id, UpdateUserRequest updateUserRequest) {

        var userUpdates = userMapper.toUser(updateUserRequest);
        var updatedUser = updateUserUseCase.execute(id, userUpdates);
        return ResponseEntity.ok(userMapper.toUserResponse(updatedUser));
    }

    @Override
    public ResponseEntity<Void> changePassword(ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}