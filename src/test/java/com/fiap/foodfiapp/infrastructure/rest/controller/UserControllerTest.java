package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.user.CreateUserUseCase;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.infrastructure.rest.mapper.UserRequestMapper;
import com.fiap.foodfiapp.infrastructure.rest.mapper.UserResponseMapper;
import com.fiap.foodfiapp.model.UserRequest;
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
    private UserRepositoryGateway userRepositoryGateway;

    private UserController userController;

    private ObjectMapper objectMapper;
    private UserRequest userRequestDTO;
    private User user;
    private UUID userId;
    private UserType userType;

    private MockedStatic<UserRequestMapper> userRequestMapperMockedStatic;
    private MockedStatic<UserResponseMapper> userResponseMapperMockedStatic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(createUserUseCase, userRepositoryGateway);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        objectMapper = new ObjectMapper();

        userId = UUID.randomUUID();
        userType = new UserType(UUID.randomUUID(), "CLIENT");

        userRequestDTO = new UserRequest();
        userRequestDTO.setName("Test User");
        userRequestDTO.setEmail("test@email.com");
        userRequestDTO.setPassword("password123");

        user = new User(userId, "Test User", "test@email.com", "login_test", "12345678901", Collections.emptyList(), userType, true, OffsetDateTime.now(), OffsetDateTime.now(), "password123");

        userRequestMapperMockedStatic = mockStatic(UserRequestMapper.class);
        userRequestMapperMockedStatic.when(() -> UserRequestMapper.toEntity(any(UserRequest.class))).thenReturn(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setName("Test User");
        userResponse.setEmail("test@email.com");
        userResponse.setCpf("12345678901");
        userResponse.setLogin("login_test");
        userResponse.setAddresses(Collections.emptyList());
        userResponse.setUserType("CLIENT");
        userResponse.setActive(true);

        userResponseMapperMockedStatic = mockStatic(UserResponseMapper.class);
        userResponseMapperMockedStatic.when(() -> UserResponseMapper.toDTO(any(User.class))).thenReturn(userResponse);
    }

    @AfterEach
    void tearDown() {
        if (userRequestMapperMockedStatic != null) {
            userRequestMapperMockedStatic.close();
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
        when(userRepositoryGateway.save(any(User.class))).thenReturn(user);

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value(user.getName()));

        verify(userRepositoryGateway).findById(eq(userId));
        verify(userRepositoryGateway).save(any(User.class));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentUser() throws Exception {
        when(userRepositoryGateway.findById(eq(userId))).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isNotFound());

        verify(userRepositoryGateway).findById(eq(userId));
        verify(userRepositoryGateway, never()).save(any(User.class));
    }

    @Test
    void shouldReturnNoContentWhenUserIsDeleted() throws Exception {
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userRepositoryGateway).deleteById(eq(userId));
    }
}