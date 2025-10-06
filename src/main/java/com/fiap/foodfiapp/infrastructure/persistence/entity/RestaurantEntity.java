package com.fiap.foodfiapp.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String cuisineType;

    private String openingHours;

    @Column(name = "user_owner_id", nullable = false)
    private UUID userOwnerId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", unique = true)
    private AddressesEntity address;
    private String description;

    private Boolean active;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItemEntity> menuItems = new ArrayList<>();

    public RestaurantEntity(String name, String cuisineType, String openingHours, UUID userOwnerId, AddressesEntity address, Boolean active) {
        this.name = name;
        this.cuisineType = cuisineType;
        this.openingHours = openingHours;
        this.userOwnerId = userOwnerId;
        this.address = address;
        this.active = active;
    }
}
