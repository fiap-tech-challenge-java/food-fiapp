package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.user.CreateUserUseCase;
import com.fiap.foodfiapp.core.domain.entities.CreateUser;
import com.fiap.foodfiapp.core.domain.entities.User;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.infrastructure.rest.dto.UserRequestDTO;
import com.fiap.foodfiapp.infrastructure.rest.dto.UserResponseDTO;
import com.fiap.foodfiapp.infrastructure.rest.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final CreateUserUseCase createUserUseCase;
    private final UserRepositoryGateway userRepositoryGateway;
    private static final UserMapper USER_MAPPER = UserMapper.INSTANCE;

    public UserController(CreateUserUseCase createUserUseCase, UserRepositoryGateway userRepositoryGateway) {
        this.createUserUseCase = createUserUseCase;
        this.userRepositoryGateway = userRepositoryGateway;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            CreateUser createdUser = createUserUseCase.execute(USER_MAPPER.mapToCreateUser(userRequestDTO));
            UserResponseDTO responseDTO = USER_MAPPER.mapToUserResponseDTO(createdUser);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (BusinessException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        List<User> users = userRepositoryGateway.findAll();
        List<UserResponseDTO> response = UserMapper.INSTANCE.mapToUserResponseListDTO(users);

        return ResponseEntity.ok(response);
    }
}
