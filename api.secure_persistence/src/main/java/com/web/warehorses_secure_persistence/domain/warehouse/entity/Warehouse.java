package com.web.warehorses_secure_persistence.domain.warehouse.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.web.warehorses_secure_persistence.domain.company.entity.Company;
import com.web.warehorses_secure_persistence.domain.item.entity.Item;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "warehouses", uniqueConstraints = {@UniqueConstraint(columnNames = {"address"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id", updatable = false, nullable = false)
    private short warehouseId;

    @Column(name = "address", nullable = false)
    private String address;

    @ManyToOne
    @JoinColumn(name = "company_nit")
    private Company company;

    @OneToMany(mappedBy = "warehouse", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private List<Item> items;

    public Warehouse(String address, Company company) {}
}