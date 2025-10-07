package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.api.UsersApi;
import com.fiap.foodfiapp.core.application.usecases.user.CreateUserUseCase;
import com.fiap.foodfiapp.core.application.usecases.user.DeleteUserUseCase;
import com.fiap.foodfiapp.core.application.usecases.user.FindUserUseCase;
import com.fiap.foodfiapp.core.application.usecases.user.UpdateUserUseCase;
import com.fiap.foodfiapp.infrastructure.rest.mapper.UserMapper;
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

    // AGORA SÓ DEPENDE DE CASOS DE USO
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final FindUserUseCase findUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public ResponseEntity<UserResponse> createUser(CreateUserRequest createUserRequest) {
        var user = userMapper.toUser(createUserRequest);
        var createdUser = createUserUseCase.execute(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toUserResponse(createdUser));
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID id) {
        // USA O CASO DE USO DE DELEÇÃO
        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserResponse> getUser(UUID id) {
        // A LÓGICA DE BUSCA FOI MOVIDA PARA O FindUserUseCase
        return findUserUseCase.findById(id) // Supondo que você adicione este método ao FindUserUseCase
                .map(userMapper::toUserResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<UserResponse>> getUsers() {
        // USA O CASO DE USO DE BUSCA
        var users = findUserUseCase.findAll();
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
        // Lógica para mudança de senha a ser implementada
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}