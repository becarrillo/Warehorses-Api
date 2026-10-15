package com.web.warehorses_secure_persistence.domain.company.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.web.warehorses_secure_persistence.domain.user.entity.User;
import com.web.warehorses_secure_persistence.domain.warehouse.entity.Warehouse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
    @Id
    @Column(updatable = false, nullable = false)
    private String nit;

    @Column(nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "address")
    private String address;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
    private List<User> users;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
    private final List<Warehouse> warehouses = new ArrayList<>();

    @Column(name = "is_enabled")
    private boolean isEnabled;

    public Company(
            String nit,
            String name,
            String email,
            String address
    ) {
        this.nit=nit;
        this.name=name;
        this.address=address;
        this.email=email;
        this.isEnabled = true;
    }
}