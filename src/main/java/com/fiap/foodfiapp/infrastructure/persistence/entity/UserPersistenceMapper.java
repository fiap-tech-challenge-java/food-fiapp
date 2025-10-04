package com.fiap.foodfiapp.infrastructure.persistence.entity;

import com.fiap.foodfiapp.core.domain.entity.Address;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;

import java.util.stream.Collectors;

public class UserPersistenceMapper {
    private UserPersistenceMapper() {
    }

    public static UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setCpf(user.getCpf());
        entity.setEmail(user.getEmail());
        entity.setLogin(user.getLogin());
        entity.setPassword(user.getPassword());
        entity.setUserType(toPersistenceUserType(user.getUserType()));
        entity.setIsActive(user.isActive());
        if (user.getAddresses() != null) {
            entity.setAddressesList(user.getAddresses().stream()
                    .map(UserPersistenceMapper::toAddressesEntity)
                    .collect(Collectors.toList()));
        }
        return entity;
    }

    public static User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getLogin(),
                entity.getCpf(),
                entity.getAddressesList() != null ?
                        entity.getAddressesList().stream()
                                .map(UserPersistenceMapper::toAddressDomain)
                                .collect(Collectors.toList()) : null,
                toDomainUserType(entity.getUserType()),
                Boolean.TRUE.equals(entity.getIsActive()),
                                entity.getUpdatedAt(), // Parameter order must match User domain entity constructor
                entity.getCreatedAt(),
                entity.getPassword()
        );
    }

    private static AddressEntity toAddressesEntity(Address address) {
        return new AddressEntity(
                address.getId(),
                null,
                address.getPublicPlace(),
                address.getNumber(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getPostalCode()
        );
    }

    private static Address toAddressDomain(AddressEntity addressesEntity) {
        return new Address(
                addressesEntity.getId(),
                addressesEntity.getPublicPlace(),
                addressesEntity.getNumber(),
                addressesEntity.getComplement(),
                addressesEntity.getNeighborhood(),
                addressesEntity.getCity(),
                addressesEntity.getState(),
                addressesEntity.getPostalCode()
        );
    }

    private static UserTypeEntity toPersistenceUserType(UserType domainUserType) {
        if (domainUserType == null) return null;
        return UserTypeEntity.builder()
                .uuid(domainUserType.getUuid())
                .name(domainUserType.getName())
                .build();
    }

    private static UserType toDomainUserType(UserTypeEntity persistenceUserType) {
        if (persistenceUserType == null) return null;
        return new UserType(
                persistenceUserType.getUuid(),
                persistenceUserType.getName(),
                persistenceUserType.getCreatedAt(),
                persistenceUserType.getUpdatedAt()
        );
    }
}
