package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.user.CreateUserUseCase;
import com.fiap.foodfiapp.core.application.usecases.user.UpdateUserUseCase;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.EmailAlreadyExistsException;
import com.fiap.foodfiapp.infrastructure.rest.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreateUserUseCase createUserUseCase;

    @Mock
    private UpdateUserUseCase updateUserUseCase;

    @Mock
    private UserRepositoryGateway userRepositoryGateway;

    private ObjectMapper objectMapper;
    private String createUserJson;
    private String updateUserJson;
    private User user;
    private UUID userId;
    private UserType userType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        UserController userController = new UserController(createUserUseCase, updateUserUseCase, userRepositoryGateway);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();

        userId = UUID.randomUUID();
        userType = new UserType(UUID.randomUUID(), "CLIENT");

        // Build request JSONs instead of using generated DTOs
        createUserJson = objectMapper.createObjectNode()
                .put("name", "Test User")
                .put("email", "test@email.com")
                .put("password", "password123")
                .put("cpf", "12345678901")
                .put("login", "login_test")
                .put("userTypeUuid", userType.getUuid().toString())
                .put("active", true)
                .putArray("addresses")
                .toString();

        updateUserJson = objectMapper.createObjectNode()
                .put("name", "Updated User")
                .put("email", "updated@email.com")
                .toString();

        user = new User(userId, "Test User", "test@email.com", "login_test", "12345678901",
                Collections.emptyList(), userType, true, OffsetDateTime.now(), OffsetDateTime.now(), "password123");
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        List<User> userList = List.of(user);
        when(userRepositoryGateway.findAll()).thenReturn(userList);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());

        verify(userRepositoryGateway).findAll();
    }

    @Test
    void shouldReturnUserWhenFoundById() throws Exception {
        when(userRepositoryGateway.findById(eq(userId))).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk());

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
    void shouldReturnNotFoundWhenUpdatingNonExistentUser() throws Exception {
        when(userRepositoryGateway.findById(eq(userId))).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateUserJson))
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