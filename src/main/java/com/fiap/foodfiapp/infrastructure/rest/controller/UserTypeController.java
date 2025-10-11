package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.api.UserTypesApi;
import com.fiap.foodfiapp.core.application.usecases.usertype.CreateUserTypeUseCase;
import com.fiap.foodfiapp.core.application.usecases.usertype.DeleteUserTypeUseCase;
import com.fiap.foodfiapp.core.application.usecases.usertype.UpdateUserTypeUseCase;
import com.fiap.foodfiapp.core.domain.port.UserTypeRepository;
import com.fiap.foodfiapp.infrastructure.rest.mapper.UserTypeMapper;
import com.fiap.foodfiapp.infrastructure.security.AuthenticationService;
import com.fiap.foodfiapp.model.CreateUserTypeRequest;
import com.fiap.foodfiapp.model.UpdateUserTypeRequest;
import com.fiap.foodfiapp.model.UserTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserTypeController implements UserTypesApi {

    private final CreateUserTypeUseCase createUserTypeUseCase;
    private final UpdateUserTypeUseCase updateUserTypeUseCase;
    private final DeleteUserTypeUseCase deleteUserTypeUseCase;
    private final UserTypeRepository userTypeRepository;
    private final AuthenticationService authenticationService;

    private final UserTypeMapper userTypeMapper = UserTypeMapper.INSTANCE;

    @Override
    public ResponseEntity<UserTypeResponse> createUserType(CreateUserTypeRequest createUserTypeRequest) {

        var userType = userTypeMapper.toUserType(createUserTypeRequest);
        var createdUserType = createUserTypeUseCase.execute(userType);
        return ResponseEntity.status(HttpStatus.CREATED).body(userTypeMapper.toUserTypeResponse(createdUserType));
    }

    @Override
    public ResponseEntity<Void> deleteUserType(UUID uuid) {

        deleteUserTypeUseCase.execute(uuid);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserTypeResponse> getUserType(UUID uuid) {
        return userTypeRepository.findById(uuid)
                .map(userTypeMapper::toUserTypeResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<UserTypeResponse>> getUserTypes() {

        var userTypes = userTypeRepository.findAll();
        return ResponseEntity.ok(userTypeMapper.toUserTypeResponseList(userTypes));
    }

    @Override
    public ResponseEntity<UserTypeResponse> updateUserType(UUID uuid, UpdateUserTypeRequest updateUserTypeRequest) {

        var userTypeUpdates = userTypeMapper.toUserType(updateUserTypeRequest);
        var updatedUserType = updateUserTypeUseCase.execute(uuid, userTypeUpdates);
        return ResponseEntity.ok(userTypeMapper.toUserTypeResponse(updatedUserType));
    }
}