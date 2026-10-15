package com.web.warehorses_secure_persistence.domain.user.util.mapper;


import com.web.warehorses_secure_persistence.domain.user.dto.UserResponseDto;
import com.web.warehorses_secure_persistence.domain.user.dto.UserRegisterRequestDto;
import com.web.warehorses_secure_persistence.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

// componentModel = "spring" says to MapStruct to generate a Spring @Component
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE) // <--- Ignora propiedades sobrantes/faltantes)
public interface UserMapper {
    UserResponseDto toDto(User user);
    User toEntity(UserRegisterRequestDto dto);
}
