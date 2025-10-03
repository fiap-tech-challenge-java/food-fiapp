package com.fiap.foodfiapp.infrastructure.persistence.springdata;

import com.fiap.foodfiapp.infrastructure.persistence.entity.AddressesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AddressesRestaurantSpringDataRepository extends JpaRepository<AddressesEntity, UUID> {

}
