package com.web.warehorses_secure_persistence.domain.client.entity;

import com.web.warehorses_secure_persistence.domain.activity.entity.Activity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "client_id", updatable = false, nullable = false)
    private int clientId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(
            mappedBy = "client",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Activity> activities;

    public Client(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Client(int clientId,
                  String name,
                  String phoneNumber) {
        this.clientId = clientId;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}