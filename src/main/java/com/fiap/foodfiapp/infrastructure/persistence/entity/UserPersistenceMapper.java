package com.fiap.foodfiapp.infrastructure.persistence.entity;

import com.fiap.foodfiapp.core.domain.entity.Address;
import com.fiap.foodfiapp.core.domain.entity.User;
import com.fiap.foodfiapp.core.domain.entity.UserType;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

public class UserPersistenceMapper {
    private UserPersistenceMapper() {
    }

    public static UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .cpf(user.getCpf())
                .email(user.getEmail())
                .login(user.getLogin())
                .password(user.getPassword())
                .userType(toPersistenceUserType(user.getUserType()))
                .active(user.isActive())
                .addressesList(user.getAddresses() != null ?
                        user.getAddresses().stream()
                                .map(UserPersistenceMapper::toAddressesEntity)
                                .collect(Collectors.toList()) : null)
                .build();
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
                entity.isActive(),
                OffsetDateTime.now(),
                OffsetDateTime.now(),
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
        return UserTypeEntity.builder()
                .uuid(domainUserType.getUuid())
                .name(domainUserType.getName())
                .build();
    }

    private static UserType toDomainUserType(UserTypeEntity persistenceUserType) {
        return new UserType(persistenceUserType.getUuid(), persistenceUserType.getName());
    }
}
