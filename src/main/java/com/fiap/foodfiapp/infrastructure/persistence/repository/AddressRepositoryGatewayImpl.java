package com.fiap.foodfiapp.infrastructure.persistence.repository;

import com.fiap.foodfiapp.core.application.gateways.AddressRepositoryGateway;
import com.fiap.foodfiapp.core.domain.entity.Address;
import com.fiap.foodfiapp.infrastructure.persistence.entity.AddressEntity;
import com.fiap.foodfiapp.infrastructure.persistence.mapper.AddressPersistenceMapper;
import com.fiap.foodfiapp.infrastructure.persistence.springdata.AddressSpringDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AddressRepositoryGatewayImpl implements AddressRepositoryGateway {

    private final AddressSpringDataRepository repository;

    @Override
    public Address save(Address address, UUID ownerId, String ownerType) {
        AddressEntity entity = AddressPersistenceMapper.toEntity(address);
        entity.setOwnerId(ownerId);
        entity.setOwnerType(ownerType);
        AddressEntity savedEntity = repository.save(entity);
        return AddressPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public List<Address> findByOwner(UUID ownerId, String ownerType) {
        return repository.findByOwnerIdAndOwnerType(ownerId, ownerType).stream()
                .map(AddressPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID addressId) {
        repository.deleteById(addressId);
    }

    @Override
    public Optional<Address> findById(UUID addressId) {
        return repository.findById(addressId).map(AddressPersistenceMapper::toDomain);
    }
}