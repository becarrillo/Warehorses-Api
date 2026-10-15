package com.web.warehorses_secure_persistence.domain.company.util.mapper;

import com.web.warehorses_secure_persistence.domain.company.dto.CompanyRequestDto;
import com.web.warehorses_secure_persistence.domain.company.dto.CompanyResponseDto;
import com.web.warehorses_secure_persistence.domain.company.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

// componentModel = "spring" says to MapStruct to generate a Spring @Component
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE) // <--- Ignora propiedades sobrantes/faltantes)
public interface CompanyMapper {
    CompanyResponseDto toDto(Company company);
    Company toEntity(CompanyRequestDto dto);
}
