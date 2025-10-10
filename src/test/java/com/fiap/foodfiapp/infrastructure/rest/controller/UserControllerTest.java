package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.foodfiapp.core.application.usecases.user.CreateUserUseCase;
import com.fiap.foodfiapp.core.application.usecases.user.DeleteUserUseCase;
import com.fiap.foodfiapp.core.application.usecases.user.FindUserUseCase;
import com.fiap.foodfiapp.core.application.usecases.user.FindAllUserUseCase;
import com.fiap.foodfiapp.core.application.usecases.user.UpdateUserUseCase;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.infrastructure.rest.exception.GlobalExceptionHandler;
import com.fiap.foodfiapp.infrastructure.security.AuthenticationService;
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
        private FindUserUseCase findUserUseCase;

        @Mock
        private FindAllUserUseCase findAllUserUseCase;

        @Mock
        private DeleteUserUseCase deleteUserUseCase;

        @Mock
        private AuthenticationService authenticationService;

        private ObjectMapper objectMapper;
        private String createUserJson;
        private String updateUserJson;
        private User user;
        private UUID userId;
        private UserType userType;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);

                UserController userController = new UserController(createUserUseCase, updateUserUseCase,
                                findUserUseCase, findAllUserUseCase, deleteUserUseCase, authenticationService);
                mockMvc = MockMvcBuilders.standaloneSetup(userController)
                                .setControllerAdvice(new GlobalExceptionHandler())
                                .build();

                objectMapper = new ObjectMapper();

                userId = UUID.randomUUID();
                userType = new UserType(UUID.randomUUID(), "CLIENT");

                // Build request JSONs with valid CPF
                createUserJson = objectMapper.createObjectNode()
                                .put("name", "Test User")
                                .put("email", "test@email.com")
                                .put("password", "password123")
                                .put("cpf", "52998224725") // Valid CPF
                                .put("login", "login_test")
                                .put("userTypeUuid", userType.getUuid().toString())
                                .put("active", true)
                                .toString();

                updateUserJson = objectMapper.createObjectNode()
                                .put("name", "Updated User")
                                .put("email", "updated@email.com")
                                .toString();

                user = new User(userId, "Test User", "test@email.com", "login_test", "52998224725",
                                Collections.emptyList(), userType, true, OffsetDateTime.now(), OffsetDateTime.now(),
                                "password123");
        }

        @Test
        void shouldReturnAllUsersWhenUserIsAdmin() throws Exception {
                List<User> userList = List.of(user);
                when(findAllUserUseCase.execute()).thenReturn(userList);

                mockMvc.perform(get("/users"))
                                .andExpect(status().isOk());

                verify(findAllUserUseCase).execute();
        }

        @Test
        void shouldReturnUserWhenAuthorizedToViewProfile() throws Exception {
                when(findUserUseCase.execute(eq(userId))).thenReturn(Optional.of(user));

                mockMvc.perform(get("/users/{id}", userId))
                                .andExpect(status().isOk());

                verify(findUserUseCase).execute(eq(userId));
        }

        @Test
        void shouldReturnNotFoundWhenUserNotFoundById() throws Exception {
                when(findUserUseCase.execute(eq(userId))).thenReturn(Optional.empty());

                mockMvc.perform(get("/users/{id}", userId))
                                .andExpect(status().isNotFound());

                verify(findUserUseCase).execute(eq(userId));
        }

        @Test
        void shouldDeleteUserWhenAuthorizedAdmin() throws Exception {
                mockMvc.perform(delete("/users/{id}", userId))
                                .andExpect(status().isNoContent());

                verify(deleteUserUseCase).execute(eq(userId));
        }

        @Test
        void shouldUpdateUserWhenAuthorized() throws Exception {
                when(updateUserUseCase.execute(eq(userId), any())).thenReturn(user);

                mockMvc.perform(put("/users/{id}", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateUserJson))
                                .andExpect(status().isOk());

                verify(updateUserUseCase).execute(eq(userId), any());
        }


        @Test
        void shouldCreateUserSuccessfully() throws Exception {
                when(createUserUseCase.execute(any())).thenReturn(user);

                mockMvc.perform(post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createUserJson))
                                .andExpect(status().isCreated());

                verify(createUserUseCase).execute(any());
        }
}