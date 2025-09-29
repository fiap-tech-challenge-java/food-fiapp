package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.api.UserTypesApi;
import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.usertype.CreateUserTypeUseCase;
import com.fiap.foodfiapp.core.application.usecases.usertype.DeleteUserTypeUseCase;
import com.fiap.foodfiapp.core.application.usecases.usertype.UpdateUserTypeUseCase;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.infrastructure.rest.mapper.UserTypeRequestMapper;
import com.fiap.foodfiapp.infrastructure.rest.mapper.UserTypeResponseMapper;
import com.fiap.foodfiapp.model.CreateUserTypeRequest;
import com.fiap.foodfiapp.model.UpdateUserTypeRequest;
import com.fiap.foodfiapp.model.UserTypeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class UserTypeController implements UserTypesApi {
    private final CreateUserTypeUseCase createUserTypeUseCase;
    private final UpdateUserTypeUseCase updateUserTypeUseCase;
    private final DeleteUserTypeUseCase deleteUserTypeUseCase;
    private final UserTypeRepositoryGateway userTypeRepositoryGateway;

    public UserTypeController(CreateUserTypeUseCase createUserTypeUseCase,
                             UpdateUserTypeUseCase updateUserTypeUseCase,
                             DeleteUserTypeUseCase deleteUserTypeUseCase,
                             UserTypeRepositoryGateway userTypeRepositoryGateway) {
        this.createUserTypeUseCase = createUserTypeUseCase;
        this.updateUserTypeUseCase = updateUserTypeUseCase;
        this.deleteUserTypeUseCase = deleteUserTypeUseCase;
        this.userTypeRepositoryGateway = userTypeRepositoryGateway;
    }

    @Override
    public ResponseEntity<UserTypeResponse> createUserType(CreateUserTypeRequest createUserTypeRequest) {
        try {
            var userType = UserTypeRequestMapper.toEntity(createUserTypeRequest);
            var createdUserType = createUserTypeUseCase.execute(userType);
            var responseDTO = UserTypeResponseMapper.toDTO(createdUserType);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (BusinessException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteUserType(UUID uuid) {
        try {
            deleteUserTypeUseCase.execute(uuid);
            return ResponseEntity.noContent().build();
        } catch (BusinessException ex) {
            if (ex.getMessage().contains("User type not found")) {
                return ResponseEntity.notFound().build();
            } else if (ex.getMessage().contains("Cannot delete user type that is being used")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<UserTypeResponse> getUserType(UUID uuid) {
        return userTypeRepositoryGateway.findById(uuid)
                .map(UserTypeResponseMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<UserTypeResponse>> getUserTypes() {
        var userTypes = userTypeRepositoryGateway.findAll();
        var response = userTypes.stream()
                .map(UserTypeResponseMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserTypeResponse> updateUserType(UUID uuid, UpdateUserTypeRequest updateUserTypeRequest) {
        try {
            var userTypeUpdates = UserTypeRequestMapper.toEntity(updateUserTypeRequest);
            var updatedUserType = updateUserTypeUseCase.execute(uuid, userTypeUpdates);
            var responseDTO = UserTypeResponseMapper.toDTO(updatedUserType);
            return ResponseEntity.ok(responseDTO);
        } catch (BusinessException ex) {
            if (ex.getMessage().contains("User type not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
