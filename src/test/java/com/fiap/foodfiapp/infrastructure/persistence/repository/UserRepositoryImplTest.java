package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserEntity;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserTypeEntity;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.UserSpringDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private UserSpringDataRepository userSpringDataRepository;

    @InjectMocks
    private UserRepositoryImpl userRepository;

    private UUID userId;
    private UUID userTypeId;
    private User user;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        userTypeId = UUID.randomUUID();

        UserType userType = new UserType();
        userType.setUuid(userTypeId);
        userType.setName("USER");

        UserTypeEntity userTypeEntity = new UserTypeEntity();
        userTypeEntity.setUuid(userTypeId);
        userTypeEntity.setName("USER");

        user = new User();
        user.setId(userId);
        user.setName("João Silva");
        user.setEmail("joao@test.com");
        user.setCpf("12345678901");
        user.setLogin("joao@test.com");
        user.setUserType(userType);

        userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setName("João Silva");
        userEntity.setEmail("joao@test.com");
        userEntity.setCpf("12345678901");
        userEntity.setLogin("joao@test.com");
        userEntity.setUserType(userTypeEntity);
    }

    @Test
    void shouldSaveUserSuccessfully() {
        // Arrange
        when(userSpringDataRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // Act
        User result = userRepository.save(user);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("João Silva");
        assertThat(result.getEmail()).isEqualTo("joao@test.com");
        verify(userSpringDataRepository).save(any(UserEntity.class));
    }

    @Test
    void shouldFindUserById() {
        // Arrange
        when(userSpringDataRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // Act
        Optional<User> result = userRepository.findById(userId);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(userId);
        assertThat(result.get().getName()).isEqualTo("João Silva");
        verify(userSpringDataRepository).findById(userId);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFoundById() {
        // Arrange
        when(userSpringDataRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userRepository.findById(userId);

        // Assert
        assertThat(result).isEmpty();
        verify(userSpringDataRepository).findById(userId);
    }

    @Test
    void shouldFindUserByEmail() {
        // Arrange
        String email = "joao@test.com";
        when(userSpringDataRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        // Act
        Optional<User> result = userRepository.findByEmail(email);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
        verify(userSpringDataRepository).findByEmail(email);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFoundByEmail() {
        // Arrange
        String email = "notfound@test.com";
        when(userSpringDataRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userRepository.findByEmail(email);

        // Assert
        assertThat(result).isEmpty();
        verify(userSpringDataRepository).findByEmail(email);
    }

    @Test
    void shouldFindUserByCpf() {
        // Arrange
        String cpf = "12345678901";
        when(userSpringDataRepository.findByCpf(cpf)).thenReturn(Optional.of(userEntity));

        // Act
        Optional<User> result = userRepository.findByCpf(cpf);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getCpf()).isEqualTo(cpf);
        verify(userSpringDataRepository).findByCpf(cpf);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFoundByCpf() {
        // Arrange
        String cpf = "99999999999";
        when(userSpringDataRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userRepository.findByCpf(cpf);

        // Assert
        assertThat(result).isEmpty();
        verify(userSpringDataRepository).findByCpf(cpf);
    }

    @Test
    void shouldFindUserByLogin() {
        // Arrange
        String login = "joao@test.com";
        when(userSpringDataRepository.findByLogin(login)).thenReturn(Optional.of(userEntity));

        // Act
        Optional<User> result = userRepository.findByLogin(login);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getLogin()).isEqualTo(login);
        verify(userSpringDataRepository).findByLogin(login);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFoundByLogin() {
        // Arrange
        String login = "notfound@test.com";
        when(userSpringDataRepository.findByLogin(login)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userRepository.findByLogin(login);

        // Assert
        assertThat(result).isEmpty();
        verify(userSpringDataRepository).findByLogin(login);
    }

    @Test
    void shouldFindAllUsers() {
        // Arrange
        List<UserEntity> entities = List.of(userEntity);
        when(userSpringDataRepository.findAll()).thenReturn(entities);

        // Act
        List<User> result = userRepository.findAll();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("João Silva");
        verify(userSpringDataRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoUsersFound() {
        // Arrange
        when(userSpringDataRepository.findAll()).thenReturn(List.of());

        // Act
        List<User> result = userRepository.findAll();

        // Assert
        assertThat(result).isEmpty();
        verify(userSpringDataRepository).findAll();
    }

    @Test
    void shouldDeleteUserById() {
        // Arrange
        doNothing().when(userSpringDataRepository).deleteById(userId);

        // Act
        userRepository.deleteById(userId);

        // Assert
        verify(userSpringDataRepository).deleteById(userId);
    }

    @Test
    void shouldCheckIfExistsByUserTypeUuid() {
        // Arrange
        when(userSpringDataRepository.existsByUserType_Uuid(userTypeId)).thenReturn(true);

        // Act
        boolean result = userRepository.existsByUserTypeUuid(userTypeId);

        // Assert
        assertThat(result).isTrue();
        verify(userSpringDataRepository).existsByUserType_Uuid(userTypeId);
    }

    @Test
    void shouldReturnFalseWhenNoUserExistsWithUserType() {
        // Arrange
        when(userSpringDataRepository.existsByUserType_Uuid(userTypeId)).thenReturn(false);

        // Act
        boolean result = userRepository.existsByUserTypeUuid(userTypeId);

        // Assert
        assertThat(result).isFalse();
        verify(userSpringDataRepository).existsByUserType_Uuid(userTypeId);
    }
}
