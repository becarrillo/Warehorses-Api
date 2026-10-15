package com.web.warehorses_secure_persistence.domain.user.dto;

import com.web.warehorses_secure_persistence.domain.activity.dto.ActivityResponseDto;
import lombok.Builder;

import java.util.List;

@Builder
public record UserResponseDto(
        int userId,
        String fullname,
        String idCardNumber,
        UserDetailsDto.UserRole role,
        List<ActivityResponseDto> activities
) {
}
