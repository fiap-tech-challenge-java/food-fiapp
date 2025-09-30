package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserTypeEntity;
import com.fiap.foodfiapp.infrastructure.persistence.entity.UserTypePersistenceMapper;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.UserTypeSpringDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTypeRepositoryGatewayImplTest {

    @Mock
    private UserTypeSpringDataRepository userTypeSpringDataRepository;

    @InjectMocks
    private UserTypeRepositoryGatewayImpl userTypeRepositoryGateway;

    private UserType userType;
    private UserTypeEntity userTypeEntity;
    private UUID userTypeId;

    @BeforeEach
    void setUp() {
        userTypeId = UUID.randomUUID();

        userType = new UserType();
        userType.setUuid(userTypeId);
        userType.setName("CLIENT");

        userTypeEntity = new UserTypeEntity();
        userTypeEntity.setUuid(userTypeId);
        userTypeEntity.setName("CLIENT");
    }

    @Test
    void shouldSaveNewUserTypeSuccessfully() {
        UserType newUserType = new UserType();
        newUserType.setName("ADMIN");

        UserTypeEntity newEntity = new UserTypeEntity();
        newEntity.setName("ADMIN");

        UserTypeEntity savedEntity = new UserTypeEntity();
        savedEntity.setUuid(UUID.randomUUID());
        savedEntity.setName("ADMIN");

        try (MockedStatic<UserTypePersistenceMapper> mapperMock = mockStatic(UserTypePersistenceMapper.class)) {
            mapperMock.when(() -> UserTypePersistenceMapper.toEntity(newUserType)).thenReturn(newEntity);
            when(userTypeSpringDataRepository.save(newEntity)).thenReturn(savedEntity);
            mapperMock.when(() -> UserTypePersistenceMapper.toDomain(savedEntity)).thenReturn(userType);

            UserType result = userTypeRepositoryGateway.save(newUserType);

            assertThat(result).isEqualTo(userType);
            verify(userTypeSpringDataRepository).save(newEntity);
        }
    }

    @Test
    void shouldUpdateExistingUserTypeSuccessfully() {
        when(userTypeSpringDataRepository.existsById(userTypeId)).thenReturn(true);
        when(userTypeSpringDataRepository.findById(userTypeId)).thenReturn(Optional.of(userTypeEntity));

        try (MockedStatic<UserTypePersistenceMapper> mapperMock = mockStatic(UserTypePersistenceMapper.class)) {
            mapperMock.when(() -> UserTypePersistenceMapper.updateEntityFromDomain(userTypeEntity, userType))
                    .then(invocation -> null);
            when(userTypeSpringDataRepository.save(userTypeEntity)).thenReturn(userTypeEntity);
            mapperMock.when(() -> UserTypePersistenceMapper.toDomain(userTypeEntity)).thenReturn(userType);

            UserType result = userTypeRepositoryGateway.save(userType);

            assertThat(result).isEqualTo(userType);
            verify(userTypeSpringDataRepository).existsById(userTypeId);
            verify(userTypeSpringDataRepository).findById(userTypeId);
            verify(userTypeSpringDataRepository).save(userTypeEntity);
        }
    }

    @Test
    void shouldThrowRuntimeExceptionWhenEntityNotFoundForUpdate() {
        when(userTypeSpringDataRepository.existsById(userTypeId)).thenReturn(true);
        when(userTypeSpringDataRepository.findById(userTypeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userTypeRepositoryGateway.save(userType))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Entity not found");

        verify(userTypeSpringDataRepository).existsById(userTypeId);
        verify(userTypeSpringDataRepository).findById(userTypeId);
    }

    @Test
    void shouldFindUserTypeByIdSuccessfully() {
        when(userTypeSpringDataRepository.findById(userTypeId)).thenReturn(Optional.of(userTypeEntity));

        try (MockedStatic<UserTypePersistenceMapper> mapperMock = mockStatic(UserTypePersistenceMapper.class)) {
            mapperMock.when(() -> UserTypePersistenceMapper.toDomain(userTypeEntity)).thenReturn(userType);

            Optional<UserType> result = userTypeRepositoryGateway.findById(userTypeId);

            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(userType);
            verify(userTypeSpringDataRepository).findById(userTypeId);
        }
    }

    @Test
    void shouldReturnEmptyWhenUserTypeNotFoundById() {
        when(userTypeSpringDataRepository.findById(userTypeId)).thenReturn(Optional.empty());

        Optional<UserType> result = userTypeRepositoryGateway.findById(userTypeId);

        assertThat(result).isEmpty();
        verify(userTypeSpringDataRepository).findById(userTypeId);
    }

    @Test
    void shouldFindUserTypeByNameSuccessfully() {
        String name = "CLIENT";
        when(userTypeSpringDataRepository.findByName(name)).thenReturn(Optional.of(userTypeEntity));

        try (MockedStatic<UserTypePersistenceMapper> mapperMock = mockStatic(UserTypePersistenceMapper.class)) {
            mapperMock.when(() -> UserTypePersistenceMapper.toDomain(userTypeEntity)).thenReturn(userType);

            Optional<UserType> result = userTypeRepositoryGateway.findByName(name);

            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(userType);
            verify(userTypeSpringDataRepository).findByName(name);
        }
    }

    @Test
    void shouldReturnEmptyWhenUserTypeNotFoundByName() {
        String name = "NONEXISTENT";
        when(userTypeSpringDataRepository.findByName(name)).thenReturn(Optional.empty());

        Optional<UserType> result = userTypeRepositoryGateway.findByName(name);

        assertThat(result).isEmpty();
        verify(userTypeSpringDataRepository).findByName(name);
    }

    @Test
    void shouldFindAllUserTypesSuccessfully() {
        List<UserTypeEntity> entities = Arrays.asList(userTypeEntity);
        when(userTypeSpringDataRepository.findAll()).thenReturn(entities);

        try (MockedStatic<UserTypePersistenceMapper> mapperMock = mockStatic(UserTypePersistenceMapper.class)) {
            mapperMock.when(() -> UserTypePersistenceMapper.toDomain(userTypeEntity)).thenReturn(userType);

            List<UserType> result = userTypeRepositoryGateway.findAll();

            assertThat(result).hasSize(1);
            assertThat(result.get(0)).isEqualTo(userType);
            verify(userTypeSpringDataRepository).findAll();
        }
    }

    @Test
    void shouldReturnEmptyListWhenNoUserTypesFound() {
        when(userTypeSpringDataRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserType> result = userTypeRepositoryGateway.findAll();

        assertThat(result).isEmpty();
        verify(userTypeSpringDataRepository).findAll();
    }

    @Test
    void shouldDeleteUserTypeByIdSuccessfully() {
        doNothing().when(userTypeSpringDataRepository).deleteById(userTypeId);

        userTypeRepositoryGateway.deleteById(userTypeId);

        verify(userTypeSpringDataRepository).deleteById(userTypeId);
    }

    @Test
    void shouldHandleMultipleUserTypesInFindAll() {
        UserTypeEntity secondEntity = new UserTypeEntity();
        secondEntity.setUuid(UUID.randomUUID());
        secondEntity.setName("ADMIN");

        UserType secondUserType = new UserType();
        secondUserType.setUuid(secondEntity.getUuid());
        secondUserType.setName("ADMIN");

        List<UserTypeEntity> entities = Arrays.asList(userTypeEntity, secondEntity);
        when(userTypeSpringDataRepository.findAll()).thenReturn(entities);

        try (MockedStatic<UserTypePersistenceMapper> mapperMock = mockStatic(UserTypePersistenceMapper.class)) {
            mapperMock.when(() -> UserTypePersistenceMapper.toDomain(userTypeEntity)).thenReturn(userType);
            mapperMock.when(() -> UserTypePersistenceMapper.toDomain(secondEntity)).thenReturn(secondUserType);

            List<UserType> result = userTypeRepositoryGateway.findAll();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getName()).isEqualTo("CLIENT");
            assertThat(result.get(1).getName()).isEqualTo("ADMIN");
            verify(userTypeSpringDataRepository).findAll();
        }
    }
}
