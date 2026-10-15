package com.web.warehorses_secure_persistence.domain.item.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.web.warehorses_secure_persistence.domain.activity.entity.Activity;
import com.web.warehorses_secure_persistence.domain.warehouse.entity.Warehouse;
import jakarta.persistence.*;
import jdk.jfr.Unsigned;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Unsigned
    @Column(name = "item_id", updatable = false, nullable = false)
    private int itemId;

    @Column(name = "description", nullable = false)
    private String description;

    @Unsigned
    @Column(name = "unit_price", precision = 19, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "sale_price", precision = 19, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    private String supplier;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "activities_items",
            joinColumns = @JoinColumn(name = "item_id", referencedColumnName = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id", referencedColumnName = "activity_id"))
    private List<Activity> activities;

    public Item(
            String description,
            BigDecimal unitPrice,
            BigDecimal salePrice,
            int quantity
    ) {
        this.description=description;
        this.unitPrice=unitPrice;
        this.salePrice=salePrice;
        this.quantity=quantity;
    }
}