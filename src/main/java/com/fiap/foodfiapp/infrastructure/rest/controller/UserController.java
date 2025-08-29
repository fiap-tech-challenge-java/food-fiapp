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
}
