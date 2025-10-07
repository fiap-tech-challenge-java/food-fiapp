package com.fiap.foodfiapp.infrastructure.persistence.springdata;

import com.fiap.foodfiapp.infrastructure.persistence.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AddressSpringDataRepository extends JpaRepository<AddressEntity, UUID> {
    List<AddressEntity> findByOwnerIdAndOwnerType(UUID ownerId, String ownerType);
}