package com.web.warehorses_secure_persistence.domain.warehouse.repository;

import com.web.warehorses_secure_persistence.domain.warehouse.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Short> {
    Warehouse findByAddressContainingIgnoreCase(String address);
}
