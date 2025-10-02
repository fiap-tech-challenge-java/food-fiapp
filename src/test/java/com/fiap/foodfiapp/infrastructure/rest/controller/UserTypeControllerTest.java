package com.fiap.foodfiapp.infrastructure.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.foodfiapp.core.application.gateways.UserTypeRepositoryGateway;
import com.fiap.foodfiapp.core.application.usecases.usertype.CreateUserTypeUseCase;
import com.fiap.foodfiapp.core.application.usecases.usertype.DeleteUserTypeUseCase;
import com.fiap.foodfiapp.core.application.usecases.usertype.UpdateUserTypeUseCase;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.model.CreateUserTypeRequest;
import com.fiap.foodfiapp.model.UpdateUserTypeRequest;
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
    private UpdateUserTypeUseCase updateUserTypeUseCase;

    @Mock
    private DeleteUserTypeUseCase deleteUserTypeUseCase;

    @Mock
    private UserTypeRepositoryGateway userTypeRepositoryGateway;

    private UserTypeController userTypeController;

    private ObjectMapper objectMapper;
    private CreateUserTypeRequest createUserTypeRequestDTO;
    private UpdateUserTypeRequest updateUserTypeRequestDTO;
    private UserType userType;
    private UUID userTypeUuid;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userTypeController = new UserTypeController(createUserTypeUseCase, updateUserTypeUseCase,
                deleteUserTypeUseCase, userTypeRepositoryGateway);
        mockMvc = MockMvcBuilders.standaloneSetup(userTypeController).build();

        objectMapper = new ObjectMapper();
        userTypeUuid = UUID.randomUUID();

        createUserTypeRequestDTO = new CreateUserTypeRequest();
        createUserTypeRequestDTO.setName("CUSTOMER");

        updateUserTypeRequestDTO = new UpdateUserTypeRequest();
        updateUserTypeRequestDTO.setName("CUSTOMER_VIP");

        userType = new UserType();
        userType.setUuid(userTypeUuid);
        userType.setName("CUSTOMER");
    }

    @Test
    void shouldCreateUserTypeSuccessfully() throws Exception {
        when(createUserTypeUseCase.execute(any(UserType.class))).thenReturn(userType);

        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserTypeRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").value(userType.getUuid().toString()))
                .andExpect(jsonPath("$.name").value(userType.getName()));

        verify(createUserTypeUseCase).execute(any(UserType.class));
    }

    @Test
    void shouldReturnConflictWhenUserTypeCreationFails() throws Exception {
        when(createUserTypeUseCase.execute(any(UserType.class))).thenThrow(new BusinessException("Name already exists."));

        mockMvc.perform(post("/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserTypeRequestDTO)))
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
                        .content(objectMapper.writeValueAsString(updateUserTypeRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(userTypeUuid.toString()))
                .andExpect(jsonPath("$.name").value(userType.getName()));

        verify(updateUserTypeUseCase).execute(eq(userTypeUuid), any(UserType.class));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentUserType() throws Exception {
        when(updateUserTypeUseCase.execute(eq(userTypeUuid), any(UserType.class)))
                .thenThrow(new BusinessException("User type not found"));

        mockMvc.perform(put("/user-types/{uuid}", userTypeUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserTypeRequestDTO)))
                .andExpect(status().isNotFound());

        verify(updateUserTypeUseCase).execute(eq(userTypeUuid), any(UserType.class));
    }

    @Test
    void shouldReturnConflictWhenUpdateUserTypeFails() throws Exception {
        when(updateUserTypeUseCase.execute(eq(userTypeUuid), any(UserType.class)))
                .thenThrow(new BusinessException("Name already exists"));

        mockMvc.perform(put("/user-types/{uuid}", userTypeUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserTypeRequestDTO)))
                .andExpect(status().isConflict());

        verify(updateUserTypeUseCase).execute(eq(userTypeUuid), any(UserType.class));
    }

    @Test
    void shouldDeleteUserTypeSuccessfully() throws Exception {
        doNothing().when(deleteUserTypeUseCase).execute(userTypeUuid);

        mockMvc.perform(delete("/user-types/{uuid}", userTypeUuid))
                .andExpect(status().isNoContent());

        verify(deleteUserTypeUseCase).execute(userTypeUuid);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentUserType() throws Exception {
        doThrow(new BusinessException("User type not found")).when(deleteUserTypeUseCase).execute(userTypeUuid);

        mockMvc.perform(delete("/user-types/{uuid}", userTypeUuid))
                .andExpect(status().isNotFound());

        verify(deleteUserTypeUseCase).execute(userTypeUuid);
    }

    @Test
    void shouldReturnConflictWhenDeletingUserTypeInUse() throws Exception {
        doThrow(new BusinessException("Cannot delete user type that is being used")).when(deleteUserTypeUseCase).execute(userTypeUuid);

        mockMvc.perform(delete("/user-types/{uuid}", userTypeUuid))
                .andExpect(status().isConflict());

        verify(deleteUserTypeUseCase).execute(userTypeUuid);
    }

    @Test
    void shouldReturnInternalServerErrorWhenDeletingUserTypeFails() throws Exception {
        doThrow(new BusinessException("Unexpected error")).when(deleteUserTypeUseCase).execute(userTypeUuid);

        mockMvc.perform(delete("/user-types/{uuid}", userTypeUuid))
                .andExpect(status().isInternalServerError());

        verify(deleteUserTypeUseCase).execute(userTypeUuid);
    }

    @Test
    void shouldReturnEmptyListWhenNoUserTypes() throws Exception {
        when(userTypeRepositoryGateway.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/user-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(userTypeRepositoryGateway).findAll();
    }
}
