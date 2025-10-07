package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.domain.entity.UserType;
import com.fiap.foodfiapp.core.domain.port.UserTypeRepository;
import com.fiap.foodfiapp.infrastructure.persistence.mapper.UserTypePersistenceMapper;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.UserTypeSpringDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserTypeRepositoryImpl implements UserTypeRepository {

    private final UserTypeSpringDataRepository userTypeSpringDataRepository;
    private final UserTypePersistenceMapper userTypeMapper = UserTypePersistenceMapper.INSTANCE;

    @Override
    public UserType save(UserType userType) {
        var entity = userTypeMapper.toEntity(userType);
        var savedEntity = userTypeSpringDataRepository.save(entity);
        return userTypeMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<UserType> findById(UUID id) {
        return userTypeSpringDataRepository.findById(id).map(userTypeMapper::toDomain);
    }

    @Override
    public Optional<UserType> findByName(String name) {
        return userTypeSpringDataRepository.findByName(name).map(userTypeMapper::toDomain);
    }

    @Override
    public List<UserType> findAll() {
        return userTypeSpringDataRepository.findAll().stream()
                .map(userTypeMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        userTypeSpringDataRepository.deleteById(id);
    }
}