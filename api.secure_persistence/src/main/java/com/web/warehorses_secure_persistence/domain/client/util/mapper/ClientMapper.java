package com.web.warehorses_secure_persistence.domain.client.util.mapper;

import com.web.warehorses_secure_persistence.domain.client.dto.ClientResponseDto;
import com.web.warehorses_secure_persistence.domain.client.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

// componentModel = "spring" says to MapStruct to generate a Spring @Component
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE) // <--- Ignora propiedades sobrantes/faltantes)
public interface ClientMapper {
    ClientResponseDto toDto(Client client);
}
