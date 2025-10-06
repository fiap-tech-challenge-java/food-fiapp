package com.fiap.foodfiapp.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String cpf;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_type_uuid", nullable = false, referencedColumnName = "uuid")
    private UserTypeEntity userType;

    @OneToMany
    @JoinColumn(name = "user_owner_id", referencedColumnName = "id")
    private List<RestaurantEntity> restaurants;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressEntity> addressesList = new ArrayList<>();

    public UserEntity(UUID id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

}
