package com.web.warehorses_secure_persistence.domain.item.util.mapper;

import com.web.warehorses_secure_persistence.domain.item.dto.ItemResponseDto;
import com.web.warehorses_secure_persistence.domain.item.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

// componentModel = "spring" says to MapStruct to generate a Spring @Component
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE) // <--- Ignora propiedades sobrantes/faltantes)
public interface ItemMapper {
    ItemResponseDto toDto(Item item);
}
