package com.web.warehorses_secure_persistence.auth.dto;

import lombok.Builder;

@Builder
public record LoginRequestDto(
    String username,
    String password
){}