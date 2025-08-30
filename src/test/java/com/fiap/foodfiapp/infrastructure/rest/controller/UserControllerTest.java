package com.fiap.foodfiapp.infrastructure.rest.controller;


import com.fiap.foodfiapp.core.application.gateways.UserRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.user.CreateUserUseCase;
import com.fiap.foodfiapp.core.domain.entities.User;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@Import(TestSecurityConfig.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CreateUserUseCase createUserUseCase;

    @Mock
    private UserRepositoryGateway userRepositoryGateway;

    @Test
    void shouldReturnCreatedWhenUserIsCreated() throws Exception {
        User createdUser = new User(UUID.randomUUID(), "Test", "test@email.com", "1234");
        when(createUserUseCase.execute(any(User.class))).thenReturn(createdUser);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"name\":\"Test\"," +
                        "\"email\":\"test@email.com\"," +
                        "\"password\":\"1234\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.email").value("test@email.com"));
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
        when(createUserUseCase.execute(any(User.class))).thenThrow(new BusinessException("User with this email already exists."));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"name\":\"Test\"," +
                        "\"email\":\"test@email.com\"," +
                        "\"password\":\"1234\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        when(userRepositoryGateway.findAll()).thenReturn(Arrays.asList(
                new User(UUID.randomUUID(), "Test1", "test1@email.com", "1234"),
                new User(UUID.randomUUID(), "Test2", "test2@email.com", "5678")
        ));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test1"))
                .andExpect(jsonPath("$[0].email").value("test1@email.com"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Test2"))
                .andExpect(jsonPath("$[1].email").value("test2@email.com"));
    }

    @Test
    void shouldReturnEmptyListWhenNoUsers() throws Exception {
        when(userRepositoryGateway.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
