package com.web.warehorses_secure_persistence.domain.warehouse.util.mapper;

import com.web.warehorses_secure_persistence.domain.warehouse.dto.WarehouseResponseDto;
import com.web.warehorses_secure_persistence.domain.warehouse.entity.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

// componentModel = "spring" says to MapStruct to generate a Spring @Component
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE) // <--- Ignora propiedades sobrantes/faltantes)
public interface WarehouseMapper {
    WarehouseResponseDto toDto(Warehouse warehouse);
}
