package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.user.CreateUserUseCase;
import com.fiap.foodfiapp.core.application.usecases.user.UpdateUserUseCase;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.infrastructure.rest.mapper.CreateUserRequestMapper;
import com.fiap.foodfiapp.infrastructure.rest.mapper.UpdateUserRequestMapper;
import com.fiap.foodfiapp.infrastructure.rest.mapper.UserResponseMapper;
import com.fiap.foodfiapp.model.CreateUserRequest;
import com.fiap.foodfiapp.model.UpdateUserRequest;
import com.fiap.foodfiapp.model.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreateUserUseCase createUserUseCase;

    @Mock
    private UpdateUserUseCase updateUserUseCase;

    @Mock
    private UserRepositoryGateway userRepositoryGateway;

    private UserController userController;

    private ObjectMapper objectMapper;
    private CreateUserRequest userRequestDTO;
    private UpdateUserRequest updateUserRequestDTO;
    private User user;
    private UUID userId;
    private UserType userType;

    private MockedStatic<CreateUserRequestMapper> createUserRequestMapperMockedStatic;
    private MockedStatic<UpdateUserRequestMapper> updateUserRequestMapperMockedStatic;
    private MockedStatic<UserResponseMapper> userResponseMapperMockedStatic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(createUserUseCase, updateUserUseCase, userRepositoryGateway);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        objectMapper = new ObjectMapper();

        userId = UUID.randomUUID();
        userType = new UserType(UUID.randomUUID(), "CLIENT");

        userRequestDTO = new CreateUserRequest();
        userRequestDTO.setName("Test User");
        userRequestDTO.setEmail("test@email.com");
        userRequestDTO.setPassword("password123");
        userRequestDTO.setCpf("12345678901");
        userRequestDTO.setLogin("login_test");
        userRequestDTO.setUserTypeUuid(userType.getUuid());
        userRequestDTO.setActive(true);
        userRequestDTO.setAddresses(Collections.emptyList());

        updateUserRequestDTO = new UpdateUserRequest();
        updateUserRequestDTO.setName("Updated User");
        updateUserRequestDTO.setEmail("updated@email.com");

        user = new User(userId, "Test User", "test@email.com", "login_test", "12345678901", Collections.emptyList(), userType, true, OffsetDateTime.now(), OffsetDateTime.now(), "password123");

        createUserRequestMapperMockedStatic = mockStatic(CreateUserRequestMapper.class);
        createUserRequestMapperMockedStatic.when(() -> CreateUserRequestMapper.toEntity(any(CreateUserRequest.class))).thenReturn(user);

        updateUserRequestMapperMockedStatic = mockStatic(UpdateUserRequestMapper.class);
        updateUserRequestMapperMockedStatic.when(() -> UpdateUserRequestMapper.toEntity(any(UpdateUserRequest.class))).thenReturn(user);

        UserResponse userResponse = new UserResponse()
                .id(userId)
                .name("Test User")
                .email("test@email.com")
                .cpf("12345678901")
                .login("login_test")
                .addresses(Collections.emptyList())
                .userType("CLIENT")
                .active(true);

        userResponseMapperMockedStatic = mockStatic(UserResponseMapper.class);
        userResponseMapperMockedStatic.when(() -> UserResponseMapper.toDTO(any(User.class))).thenReturn(userResponse);
    }

    @AfterEach
    void tearDown() {
        if (createUserRequestMapperMockedStatic != null) {
            createUserRequestMapperMockedStatic.close();
        }
        if (updateUserRequestMapperMockedStatic != null) {
            updateUserRequestMapperMockedStatic.close();
        }
        if (userResponseMapperMockedStatic != null) {
            userResponseMapperMockedStatic.close();
        }
    }

    @Test
    void shouldReturnCreatedWhenUserIsCreatedSuccessfully() throws Exception {
        when(createUserUseCase.execute(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user.getId().toString()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));

        verify(createUserUseCase).execute(any(User.class));
    }

    @Test
    void shouldReturnConflictWhenUserCreationFails() throws Exception {
        when(createUserUseCase.execute(any(User.class))).thenThrow(new BusinessException("Email already exists."));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isConflict());

        verify(createUserUseCase).execute(any(User.class));
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        List<User> userList = List.of(user);
        when(userRepositoryGateway.findAll()).thenReturn(userList);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(user.getId().toString()))
                .andExpect(jsonPath("$[0].name").value(user.getName()));

        verify(userRepositoryGateway).findAll();
    }

    @Test
    void shouldReturnUserWhenFoundById() throws Exception {
        when(userRepositoryGateway.findById(eq(userId))).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value(user.getName()));

        verify(userRepositoryGateway).findById(eq(userId));
    }

    @Test
    void shouldReturnNotFoundWhenUserNotFoundById() throws Exception {
        when(userRepositoryGateway.findById(eq(userId))).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userRepositoryGateway).findById(eq(userId));
    }

    @Test
    void shouldReturnOkWhenUserIsUpdated() throws Exception {
        when(userRepositoryGateway.findById(eq(userId))).thenReturn(Optional.of(user));
        when(updateUserUseCase.execute(eq(userId), any(User.class))).thenReturn(user);

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value(user.getName()));

        verify(userRepositoryGateway).findById(eq(userId));
        verify(updateUserUseCase).execute(eq(userId), any(User.class));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentUser() throws Exception {
        when(userRepositoryGateway.findById(eq(userId))).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequestDTO)))
                .andExpect(status().isNotFound());

        verify(userRepositoryGateway).findById(eq(userId));
        verify(updateUserUseCase, never()).execute(any(UUID.class), any(User.class));
    }

    @Test
    void shouldReturnNoContentWhenUserIsDeleted() throws Exception {
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userRepositoryGateway).deleteById(eq(userId));
    }

    @Test
    void shouldReturnBadRequestWhenUserTypeNotFoundDuringCreation() throws Exception {
        when(createUserUseCase.execute(any(User.class))).thenThrow(new BusinessException("User type not found"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isBadRequest());

        verify(createUserUseCase).execute(any(User.class));
    }

    @Test
    void shouldReturnConflictWhenUpdateUserFails() throws Exception {
        when(userRepositoryGateway.findById(eq(userId))).thenReturn(Optional.of(user));
        when(updateUserUseCase.execute(eq(userId), any(User.class))).thenThrow(new BusinessException("Email already exists"));

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequestDTO)))
                .andExpect(status().isConflict());

        verify(userRepositoryGateway).findById(eq(userId));
        verify(updateUserUseCase).execute(eq(userId), any(User.class));
    }

    @Test
    void shouldReturnNotImplementedForChangePassword() throws Exception {
        mockMvc.perform(post("/users/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"oldPassword\":\"old\",\"newPassword\":\"new\"}"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldReturnEmptyListWhenNoUsers() throws Exception {
        when(userRepositoryGateway.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(userRepositoryGateway).findAll();
    }
}