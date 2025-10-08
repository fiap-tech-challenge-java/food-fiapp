package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.domain.entity.Addresses;
import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.infrastructure.persistence.mapper.AddressesPersistenceMapper;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.AddressesSpringDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddressesRepositoryImpl implements AddressRepository {

    private final AddressesSpringDataRepository repository;
    private final AddressesPersistenceMapper addressMapper = AddressesPersistenceMapper.INSTANCE;

    @Override
    public Addresses save(Addresses addresses, UUID ownerId, String ownerType) {
        var entity = addressMapper.toEntity(addresses);
        entity.setOwnerId(ownerId);
        entity.setOwnerType(ownerType);
        var savedEntity = repository.save(entity);
        return addressMapper.toDomain(savedEntity);
    }

    @Override
    public List<Addresses> findByOwner(UUID ownerId, String ownerType) {
        var entities = repository.findByOwnerIdAndOwnerType(ownerId, ownerType);
        return addressMapper.toDomainList(entities);
    }

    @Override
    public void delete(UUID addressId) {
        repository.deleteById(addressId);
    }

    @Override
    public Optional<Addresses> findById(UUID addressId) {
        return repository.findById(addressId).map(addressMapper::toDomain);
    }

    @Override
    public Optional<Addresses> findByIdAndOwnerId(UUID id, UUID ownerId) {
        return repository.findByIdAndOwnerId(id, ownerId).map(addressMapper::toDomain);
    }
}