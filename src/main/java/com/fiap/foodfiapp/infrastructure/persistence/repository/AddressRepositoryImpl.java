package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.domain.port.AddressRepository;
import com.fiap.foodfiapp.core.domain.entity.Address;
import com.fiap.foodfiapp.infrastructure.persistence.mapper.AddressPersistenceMapper;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.AddressSpringDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddressRepositoryImpl implements AddressRepository {

    private final AddressSpringDataRepository repository;
    private final AddressPersistenceMapper addressMapper = AddressPersistenceMapper.INSTANCE;

    @Override
    public Address save(Address address, UUID ownerId, String ownerType) {
        var entity = addressMapper.toEntity(address);
        entity.setOwnerId(ownerId);
        entity.setOwnerType(ownerType);
        var savedEntity = repository.save(entity);
        return addressMapper.toDomain(savedEntity);
    }

    @Override
    public List<Address> findByOwner(UUID ownerId, String ownerType) {
        var entities = repository.findByOwnerIdAndOwnerType(ownerId, ownerType);
        return addressMapper.toDomainList(entities);
    }

    @Override
    public void delete(UUID addressId) {
        repository.deleteById(addressId);
    }

    @Override
    public Optional<Address> findById(UUID addressId) {
        return repository.findById(addressId).map(addressMapper::toDomain);
    }
}