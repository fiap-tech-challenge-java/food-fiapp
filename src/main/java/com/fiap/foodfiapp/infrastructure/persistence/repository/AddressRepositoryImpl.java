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
        // A lógica de atribuir o 'owner' foi movida para o UseCase,
        // mas a entidade JPA ainda precisa desses campos. O ideal é que o
        // UseCase prepare a entidade de domínio completa, ou que o save
        // receba os dados do owner. Vamos assumir que o UseCase enriquece o objeto.
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