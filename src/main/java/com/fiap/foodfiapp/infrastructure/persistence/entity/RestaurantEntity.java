package com.fiap.foodfiapp.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
public class RestaurantEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String cuisineType;

    private String openingHours;

    @Column(name = "user_owner_id", nullable = false)
    private UUID userOwnerId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", unique = true)
    private AddressesEntity address;

    private Boolean active;

    public RestaurantEntity(String name, String cuisineType, String openingHours, UUID userOwnerId, AddressesEntity address, Boolean active) {
        this.name = name;
        this.cuisineType = cuisineType;
        this.openingHours = openingHours;
        this.userOwnerId = userOwnerId;
        this.address = address;
        this.active = active;
    }
}
