package com.fiap.foodfiapp.infrastructure.security;

import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.exception.UnauthorizedException;
import com.fiap.foodfiapp.core.domain.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User regularUser;
    private User adminUser;
    private UserType regularUserType;
    private UserType adminUserType;
    private UUID regularUserId;
    private UUID adminUserId;

    @BeforeEach
    void setUp() {
        regularUserId = UUID.randomUUID();
        adminUserId = UUID.randomUUID();

        regularUserType = new UserType();
        regularUserType.setName("USER");

        adminUserType = new UserType();
        adminUserType.setName("ADMIN");

        regularUser = new User();
        regularUser.setId(regularUserId);
        regularUser.setLogin("user@test.com");
        regularUser.setUserType(regularUserType);

        adminUser = new User();
        adminUser.setId(adminUserId);
        adminUser.setLogin("admin@test.com");
        adminUser.setUserType(adminUserType);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldGetCurrentUserSuccessfully() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("user@test.com");
        when(authentication.getName()).thenReturn("user@test.com");
        when(userRepository.findByLogin("user@test.com")).thenReturn(Optional.of(regularUser));

        // Act
        User result = authenticationService.getCurrentUser();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getLogin()).isEqualTo("user@test.com");
        verify(userRepository).findByLogin("user@test.com");
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenAuthenticationIsNull() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(null);

        // Act & Assert
        assertThatThrownBy(() -> authenticationService.getCurrentUser())
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("User not authenticated");
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenNotAuthenticated() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> authenticationService.getCurrentUser())
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("User not authenticated");
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenPrincipalIsAnonymous() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("anonymousUser");

        // Act & Assert
        assertThatThrownBy(() -> authenticationService.getCurrentUser())
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("User not authenticated");
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenUserNotFound() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("user@test.com");
        when(authentication.getName()).thenReturn("user@test.com");
        when(userRepository.findByLogin("user@test.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> authenticationService.getCurrentUser())
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("Invalid user credentials");
    }

    @Test
    void shouldReturnTrueWhenCurrentUserIsAdmin() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("admin@test.com");
        when(authentication.getName()).thenReturn("admin@test.com");
        when(userRepository.findByLogin("admin@test.com")).thenReturn(Optional.of(adminUser));

        // Act
        boolean result = authenticationService.isCurrentUserAdmin();

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseWhenCurrentUserIsNotAdmin() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("user@test.com");
        when(authentication.getName()).thenReturn("user@test.com");
        when(userRepository.findByLogin("user@test.com")).thenReturn(Optional.of(regularUser));

        // Act
        boolean result = authenticationService.isCurrentUserAdmin();

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseWhenCannotGetCurrentUser() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(null);

        // Act
        boolean result = authenticationService.isCurrentUserAdmin();

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void shouldAllowAccessWhenUserAccessesOwnProfile() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("user@test.com");
        when(authentication.getName()).thenReturn("user@test.com");
        when(userRepository.findByLogin("user@test.com")).thenReturn(Optional.of(regularUser));

        // Act
        boolean result = authenticationService.canAccessUserProfile(regularUserId);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void shouldAllowAccessWhenAdminAccessesAnyProfile() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("admin@test.com");
        when(authentication.getName()).thenReturn("admin@test.com");
        when(userRepository.findByLogin("admin@test.com")).thenReturn(Optional.of(adminUser));

        // Act
        boolean result = authenticationService.canAccessUserProfile(regularUserId);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void shouldDenyAccessWhenUserTriesToAccessOtherProfile() {
        // Arrange
        UUID otherUserId = UUID.randomUUID();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("user@test.com");
        when(authentication.getName()).thenReturn("user@test.com");
        when(userRepository.findByLogin("user@test.com")).thenReturn(Optional.of(regularUser));

        // Act
        boolean result = authenticationService.canAccessUserProfile(otherUserId);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void shouldAllowModifyWhenUserModifiesOwnProfile() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("user@test.com");
        when(authentication.getName()).thenReturn("user@test.com");
        when(userRepository.findByLogin("user@test.com")).thenReturn(Optional.of(regularUser));

        // Act
        boolean result = authenticationService.canModifyUserProfile(regularUserId);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void shouldAllowDeleteOnlyForAdmin() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("admin@test.com");
        when(authentication.getName()).thenReturn("admin@test.com");
        when(userRepository.findByLogin("admin@test.com")).thenReturn(Optional.of(adminUser));

        // Act
        boolean result = authenticationService.canDeleteUserProfile(regularUserId);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void shouldDenyDeleteForRegularUser() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("user@test.com");
        when(authentication.getName()).thenReturn("user@test.com");
        when(userRepository.findByLogin("user@test.com")).thenReturn(Optional.of(regularUser));

        // Act
        boolean result = authenticationService.canDeleteUserProfile(regularUserId);

        // Assert
        assertThat(result).isFalse();
    }
}
