package com.web.warehorses_secure_persistence.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.web.warehorses_secure_persistence.domain.activity.entity.Activity;
import com.web.warehorses_secure_persistence.domain.company.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String fullname;

    @Column(name = "id_card_number", nullable = false)
    private String idCardNumber;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "company_nit")
    private Company company;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<Activity> activities;

    public User(String idCardNumber,
                Company company,
                String password,
                String fullname) {
        this.idCardNumber=idCardNumber;
        this.company = company;
        this.password=password;
        this.fullname=fullname;
        this.isEnabled = true;
        this.role = UserRole.USER;
    }

    public enum UserRole {
        ADMIN("ADMIN"),
        MANAGER("MANAGER"),
        USER("USER");

        final String name;

        UserRole(final String name) {
            this.name = name;
        }
    }
}

