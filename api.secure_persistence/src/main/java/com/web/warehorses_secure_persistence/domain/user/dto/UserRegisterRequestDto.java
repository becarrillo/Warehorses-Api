package com.web.warehorses_secure_persistence.domain.user.dto;

import lombok.Builder;

@Builder
public record UserRegisterRequestDto(
        String idCardNumber,
        String companyNit,
        String password,
        String fullname
){}
