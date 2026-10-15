package com.web.warehorses_secure_persistence.domain.warehouse.service;

import com.web.warehorses_secure_persistence.domain.warehouse.entity.Warehouse;
import com.web.warehorses_secure_persistence.domain.warehouse.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public Warehouse create(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    public Optional<Warehouse> findById(Short id) {
        return warehouseRepository.findById(id);
    }

    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    public Warehouse findByAddressContainingIgnoreCase(String address) {
        return warehouseRepository.findByAddressContainingIgnoreCase(address);
    }

    public Warehouse update(Short id, Warehouse warehouse) {
        Warehouse existing = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
        existing.setAddress(warehouse.getAddress());
        return warehouseRepository.save(existing);
    }

    public void delete(Short id) {
        warehouseRepository.deleteById(id);
    }
}