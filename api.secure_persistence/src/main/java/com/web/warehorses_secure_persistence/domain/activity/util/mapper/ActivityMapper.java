package com.web.warehorses_secure_persistence.domain.activity.util.mapper;

import com.web.warehorses_secure_persistence.domain.activity.dto.ActivityRequestDto;
import com.web.warehorses_secure_persistence.domain.activity.dto.ActivityResponseDto;
import com.web.warehorses_secure_persistence.domain.activity.entity.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

// componentModel = "spring" says to MapStruct to generate a Spring @Component
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE) // <--- Ignora propiedades sobrantes/faltantes)
public interface ActivityMapper {
    Activity toEntity(ActivityRequestDto dto);

    ActivityResponseDto toDto(Activity activity);
}
