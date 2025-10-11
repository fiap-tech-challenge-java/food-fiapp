package com.fiap.foodfiapp.core.application.usecases.usertype;

import com.fiap.foodfiapp.core.domain.port.UserTypeRepository;
import com.fiap.foodfiapp.core.application.usecases.usertype.impl.CreateUserTypeUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import com.fiap.foodfiapp.core.domain.exception.UserTypeNameAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateUserTypeUseCaseTest {

    @Mock
    private UserTypeRepository userTypeRepository;

    @Captor
    private ArgumentCaptor<UserType> userTypeCaptor;

    private CreateUserTypeUseCaseImpl createUserTypeUseCase;

    private UserType userType;
    private final String userTypeName = "ADMIN";
    private final UUID userTypeId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createUserTypeUseCase = new CreateUserTypeUseCaseImpl(userTypeRepository);

        userType = new UserType();
        userType.setName(userTypeName);
    }

    @Test
    void shouldCreateNewUserTypeSuccessfully() {
        // Arrange
        when(userTypeRepository.findByName(userTypeName)).thenReturn(Optional.empty());
        when(userTypeRepository.save(any(UserType.class))).thenAnswer(invocation -> {
            UserType saved = invocation.getArgument(0);
            saved.setUuid(userTypeId);
            return saved;
        });

        // Act
        UserType result = createUserTypeUseCase.execute(userType);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getUuid());
        assertEquals(userTypeName, result.getName());
        assertTrue(result.isActive(), "New user type should be active by default");
        
        verify(userTypeRepository).findByName(userTypeName);
        verify(userTypeRepository).save(userTypeCaptor.capture());
        
        UserType savedUserType = userTypeCaptor.getValue();
        assertTrue(savedUserType.isActive(), "UserType should be set as active before saving");
    }

    @Test
    void shouldThrowExceptionWhenNameAlreadyExists() {
        // Arrange
        UserType existingUserType = new UserType();
        existingUserType.setUuid(UUID.randomUUID());
        existingUserType.setName(userTypeName);
        
        when(userTypeRepository.findByName(userTypeName)).thenReturn(Optional.of(existingUserType));

        // Act & Assert
        UserTypeNameAlreadyExistsException exception = assertThrows(
            UserTypeNameAlreadyExistsException.class,
            () -> createUserTypeUseCase.execute(userType)
        );
        
        assertEquals("User type with name '" + userTypeName + "' already exists.", exception.getMessage());
        verify(userTypeRepository).findByName(userTypeName);
        verify(userTypeRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUuidAlreadyExists() {
        // Arrange
        userType.setUuid(userTypeId);
        
        when(userTypeRepository.findByName(userTypeName)).thenReturn(Optional.empty());
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.of(new UserType()));

        // Act & Assert
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> createUserTypeUseCase.execute(userType)
        );
        
        assertEquals("UserType with UUID already exists", exception.getMessage());
        verify(userTypeRepository).findById(userTypeId);
        verify(userTypeRepository, never()).save(any());
    }

    @Test
    void shouldGenerateUuidWhenNotProvided() {
        // Arrange
        when(userTypeRepository.findByName(userTypeName)).thenReturn(Optional.empty());
        when(userTypeRepository.save(any(UserType.class))).thenAnswer(invocation -> {
            UserType saved = invocation.getArgument(0);
            // The UUID is already set by the use case before save is called
            return saved;
        });

        // Act
        UserType result = createUserTypeUseCase.execute(userType);

        // Assert
        assertNotNull(result.getUuid());
        verify(userTypeRepository).save(userTypeCaptor.capture());
        // The UUID is set before save, so it should not be null in the captured value
        assertNotNull(userTypeCaptor.getValue().getUuid(), "UUID should be set before save");
    }

    @Test
    void shouldKeepProvidedUuid() {
        // Arrange
        userType.setUuid(userTypeId);
        
        when(userTypeRepository.findByName(userTypeName)).thenReturn(Optional.empty());
        when(userTypeRepository.findById(userTypeId)).thenReturn(Optional.empty());
        when(userTypeRepository.save(any(UserType.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserType result = createUserTypeUseCase.execute(userType);

        // Assert
        assertEquals(userTypeId, result.getUuid());
        verify(userTypeRepository).save(userTypeCaptor.capture());
        assertEquals(userTypeId, userTypeCaptor.getValue().getUuid());
    }

    @Test
    void shouldSetDefaultActiveStatus() {
        // Arrange
        userType.setActive(false); // Try to set inactive
        
        when(userTypeRepository.findByName(userTypeName)).thenReturn(Optional.empty());
        when(userTypeRepository.save(any(UserType.class))).thenAnswer(invocation -> {
            UserType saved = invocation.getArgument(0);
            saved.setUuid(userTypeId);
            return saved;
        });

        // Act
        UserType result = createUserTypeUseCase.execute(userType);

        // Assert
        assertTrue(result.isActive(), "New user type should be active regardless of input");
        verify(userTypeRepository).save(userTypeCaptor.capture());
        assertTrue(userTypeCaptor.getValue().isActive(), "UserType should be set as active before saving");
    }
}
