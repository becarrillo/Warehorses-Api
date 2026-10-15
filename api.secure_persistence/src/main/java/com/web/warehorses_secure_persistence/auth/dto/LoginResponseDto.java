package com.web.warehorses_secure_persistence.auth.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LoginResponseDto(
    int userId,
    String userCompanyNit,
    String userCompanyName,
    String userCompanyEmail,
    LocalDateTime timestamp
) {}