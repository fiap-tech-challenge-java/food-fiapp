package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.usertype.CreateUserTypeUseCase;
import com.fiap.foodfiapp.core.application.usecases.usertype.DeleteUserTypeUseCase;
import com.fiap.foodfiapp.core.application.usecases.usertype.UpdateUserTypeUseCase;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.infrastructure.rest.mapper.UserTypeRequestMapper;
import com.fiap.foodfiapp.infrastructure.rest.mapper.UserTypeResponseMapper;
import com.fiap.foodfiapp.model.UserTypeRequest;
import com.fiap.foodfiapp.model.UserTypeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user-types")
public class UserTypeController {
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

    @PostMapping
    public ResponseEntity<UserTypeResponse> createUserType(@RequestBody UserTypeRequest userTypeRequestDTO) {
        try {
            var userType = UserTypeRequestMapper.toEntity(userTypeRequestDTO);
            var createdUserType = createUserTypeUseCase.execute(userType);
            var responseDTO = UserTypeResponseMapper.toDTO(createdUserType);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (BusinessException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<UserTypeResponse>> findAll() {
        var userTypes = userTypeRepositoryGateway.findAll();
        var response = userTypes.stream()
                .map(UserTypeResponseMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UserTypeResponse> findById(@PathVariable UUID uuid) {
        return userTypeRepositoryGateway.findById(uuid)
                .map(UserTypeResponseMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<UserTypeResponse> updateUserType(@PathVariable UUID uuid, @RequestBody UserTypeRequest userTypeRequestDTO) {
        try {
            var userTypeUpdates = UserTypeRequestMapper.toEntity(userTypeRequestDTO);
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

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteUserType(@PathVariable UUID uuid) {
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
}
