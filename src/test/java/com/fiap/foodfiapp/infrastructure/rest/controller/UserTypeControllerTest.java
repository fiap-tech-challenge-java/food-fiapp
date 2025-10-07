package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.usertype.CreateUserTypeUseCase;
import com.fiap.foodfiapp.core.application.usecases.usertype.impl.DeleteUserTypeUseCaseImpl;
import com.fiap.foodfiapp.core.application.usecases.usertype.impl.UpdateUserTypeUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.UserTypeInUseException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeNameAlreadyExistsException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeNotFoundException;
import com.fiap.foodfiapp.infrastructure.rest.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserTypeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreateUserTypeUseCase createUserTypeUseCase;

    @Mock
    private UpdateUserTypeUseCaseImpl updateUserTypeUseCase;

    @Mock
    private DeleteUserTypeUseCaseImpl deleteUserTypeUseCase;

    @Mock
    private UserTypeRepositoryGateway userTypeRepositoryGateway;

    private UserTypeController userTypeController;

    private ObjectMapper objectMapper;
    private String createUserTypeJson;
    private String updateUserTypeJson;
    private UserType userType;
    private UUID userTypeUuid;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userTypeController = new UserTypeController(createUserTypeUseCase, updateUserTypeUseCase,
                deleteUserTypeUseCase, userTypeRepositoryGateway);
        mockMvc = MockMvcBuilders.standaloneSetup(userTypeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        userTypeUuid = UUID.randomUUID();

        // Build JSON bodies instead of using generated DTOs
        createUserTypeJson = objectMapper.createObjectNode()
                .put("name", "CUSTOMER")
                .toString();

        updateUserTypeJson = objectMapper.createObjectNode()
                .put("name", "CUSTOMER_VIP")
                .toString();

        userType = new UserType();
        userType.setUuid(userTypeUuid);
        userType.setName("CUSTOMER");
    }

    @Test
    void shouldCreateUserTypeSuccessfully() throws Exception {
        when(createUserTypeUseCase.execute(any(UserType.class))).thenReturn(userType);

        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUserTypeJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").value(userType.getUuid().toString()))
                .andExpect(jsonPath("$.name").value(userType.getName()));

        verify(createUserTypeUseCase).execute(any(UserType.class));
    }

    @Test
    void shouldReturnConflictWhenUserTypeCreationFails() throws Exception {
        when(createUserTypeUseCase.execute(any(UserType.class)))
                .thenThrow(new UserTypeNameAlreadyExistsException("CUSTOMER"));

        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUserTypeJson))
                .andExpect(status().isConflict());

        verify(createUserTypeUseCase).execute(any(UserType.class));
    }

    @Test
    void shouldReturnAllUserTypes() throws Exception {
        List<UserType> userTypes = List.of(userType);
        when(userTypeRepositoryGateway.findAll()).thenReturn(userTypes);

        mockMvc.perform(get("/user-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].uuid").value(userType.getUuid().toString()))
                .andExpect(jsonPath("$[0].name").value(userType.getName()));

        verify(userTypeRepositoryGateway).findAll();
    }

    @Test
    void shouldReturnUserTypeWhenFoundById() throws Exception {
        when(userTypeRepositoryGateway.findById(eq(userTypeUuid))).thenReturn(Optional.of(userType));

        mockMvc.perform(get("/user-types/{uuid}", userTypeUuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(userTypeUuid.toString()))
                .andExpect(jsonPath("$.name").value(userType.getName()));

        verify(userTypeRepositoryGateway).findById(eq(userTypeUuid));
    }

    @Test
    void shouldReturnNotFoundWhenUserTypeNotFoundById() throws Exception {
        when(userTypeRepositoryGateway.findById(eq(userTypeUuid))).thenReturn(Optional.empty());

        mockMvc.perform(get("/user-types/{uuid}", userTypeUuid))
                .andExpect(status().isNotFound());

        verify(userTypeRepositoryGateway).findById(eq(userTypeUuid));
    }

    @Test
    void shouldUpdateUserTypeSuccessfully() throws Exception {
        when(updateUserTypeUseCase.execute(eq(userTypeUuid), any(UserType.class))).thenReturn(userType);

        mockMvc.perform(put("/user-types/{uuid}", userTypeUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateUserTypeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(userTypeUuid.toString()))
                .andExpect(jsonPath("$.name").value(userType.getName()));

        verify(updateUserTypeUseCase).execute(eq(userTypeUuid), any(UserType.class));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentUserType() throws Exception {
        when(updateUserTypeUseCase.execute(eq(userTypeUuid), any(UserType.class)))
                .thenThrow(new UserTypeNotFoundException("User type not found."));

        mockMvc.perform(put("/user-types/{uuid}", userTypeUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateUserTypeJson))
                .andExpect(status().isNotFound());

        verify(updateUserTypeUseCase).execute(eq(userTypeUuid), any(UserType.class));
    }

    @Test
    void shouldDeleteUserTypeSuccessfully() throws Exception {
        doNothing().when(deleteUserTypeUseCase).execute(userTypeUuid);

        mockMvc.perform(delete("/user-types/{uuid}", userTypeUuid))
                .andExpect(status().isNoContent());

        verify(deleteUserTypeUseCase).execute(eq(userTypeUuid));
    }

    @Test
    void shouldReturnConflictWhenDeletingUserTypeInUse() throws Exception {
        doThrow(new UserTypeInUseException()).when(deleteUserTypeUseCase).execute(userTypeUuid);

        mockMvc.perform(delete("/user-types/{uuid}", userTypeUuid))
                .andExpect(status().isConflict());

        verify(deleteUserTypeUseCase).execute(eq(userTypeUuid));
    }
}
