package com.fiap.foodfiapp.core.application.usecases.user;

import com.fiap.foodfiapp.core.domain.port.UserRepository;
import com.fiap.foodfiapp.core.domain.port.UserTypeRepository;
import com.fiap.foodfiapp.core.application.usecases.user.impl.UpdateUserUseCaseImpl;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTypeRepository userTypeRepository;

    private UpdateUserUseCaseImpl updateUserUseCase;

    private UUID userId;
    private User existingUser;
    private User userUpdates;
    private UserType userType;
    private UserType newUserType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        updateUserUseCase = new UpdateUserUseCaseImpl(userRepository, userTypeRepository);

        userId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        // Setup user type
        userType = new UserType();
        userType.setUuid(UUID.randomUUID());
        userType.setName("CUSTOMER");
        
        // Setup existing user using constructor
        existingUser = new User(
            userId,                         // id
            "Existing User",               // name
            "existing@example.com",        // email
            "existinguser",                // login
            "11144477735",                 // cpf - Valid CPF for testing
            null,                          // addresses (can be null for test)
            userType,                      // userType
            true,                         // isActive
            now,                         // createdAt
            now,                         // updatedAt
            "password123"                // password
        );

        // Setup updates - only set the fields we want to update
        userUpdates = new User();
        userUpdates.setName("Updated User");
        userUpdates.setEmail("updated@example.com");
        userUpdates.setLogin("updateduser");
        userUpdates.setCpf("11144477735"); // Valid CPF for testing
        userUpdates.setPassword("newpassword");
        
        // Setup new user type
        newUserType = new UserType();
        newUserType.setUuid(UUID.randomUUID());
        newUserType.setName("ADMIN");
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userTypeRepository.findById(any())).thenReturn(Optional.of(userType));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = updateUserUseCase.execute(userId, userUpdates);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(userUpdates.getName(), result.getName());
        assertEquals(userUpdates.getEmail(), result.getEmail());
        assertEquals(userUpdates.getLogin(), result.getLogin());
        assertEquals(existingUser.getCreatedAt(), result.getCreatedAt());
        assertEquals(userType, result.getUserType());
        
        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldUpdateUserWithNewUserType() {
        // Arrange
        userUpdates.setUserType(newUserType);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userTypeRepository.findById(newUserType.getUuid())).thenReturn(Optional.of(newUserType));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = updateUserUseCase.execute(userId, userUpdates);

        // Assert
        assertNotNull(result);
        assertEquals(newUserType, result.getUserType());
        verify(userTypeRepository).findById(newUserType.getUuid());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> updateUserUseCase.execute(userId, userUpdates)
        );
        
        assertEquals("User not found.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserTypeNotFound() {
        // Arrange
        userUpdates.setUserType(newUserType);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userTypeRepository.findById(newUserType.getUuid())).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> updateUserUseCase.execute(userId, userUpdates)
        );
        
        assertEquals("User type not found.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Arrange
        // Create a different user with the same email
        UUID otherUserId = UUID.randomUUID();
        User anotherUser = new User(
            otherUserId,
            "Other User",
            "duplicate@example.com",  // This is the email we'll try to update to
            "otheruser",
            "98765432100",
            null,
            userType,
            true,
            OffsetDateTime.now(),
            OffsetDateTime.now(),
            "password123"
        );
        
        // Set the email in updates to a value that already exists
        userUpdates.setEmail("duplicate@example.com");
        
        // Mock the repository to return the existing user and find the duplicate email
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("duplicate@example.com")).thenReturn(Optional.of(anotherUser));
        when(userTypeRepository.findById(any())).thenReturn(Optional.of(userType));

        // Act & Assert
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> updateUserUseCase.execute(userId, userUpdates)
        );
        
        assertEquals("User with this email already exists.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldNotThrowExceptionWhenEmailIsSameAsExisting() {
        // Arrange
        userUpdates.setEmail(existingUser.getEmail());
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userTypeRepository.findById(any())).thenReturn(Optional.of(userType));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act & Assert
        assertDoesNotThrow(() -> updateUserUseCase.execute(userId, userUpdates));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldPreserveCreatedAt() {
        // Arrange
        OffsetDateTime originalCreatedAt = existingUser.getCreatedAt();
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userTypeRepository.findById(any())).thenReturn(Optional.of(userType));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = updateUserUseCase.execute(userId, userUpdates);

        // Assert
        assertEquals(originalCreatedAt, result.getCreatedAt());
    }
}
