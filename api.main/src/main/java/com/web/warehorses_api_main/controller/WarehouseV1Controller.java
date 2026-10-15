package com.web.warehorses_api_main.controller;

import com.web.warehorses_secure_persistence.domain.warehouse.service.WarehouseService;
import com.web.warehorses_secure_persistence.domain.warehouse.dto.WarehouseResponseDto;
import com.web.warehorses_secure_persistence.domain.warehouse.util.mapper.WarehouseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/warehouses")
public class WarehouseV1Controller {

    private final WarehouseService warehouseService;
    private final WarehouseMapper warehouseMapper;

    public WarehouseV1Controller(WarehouseService warehouseService, WarehouseMapper warehouseMapper) {
        this.warehouseService = warehouseService;
        this.warehouseMapper = warehouseMapper;
    }

    public ResponseEntity<List<WarehouseResponseDto>> listAllWarehouses() {
        return ResponseEntity.ok(
                warehouseService.findAll()
                        .stream()
                        .map(warehouseMapper::toDto)
                        .toList());
    }
}
