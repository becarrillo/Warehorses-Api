package com.web.warehorses_secure_persistence.domain.company.dto;

public record CompanyRequestDto(
        String nit,
        String name,
        String email,
        String address
) {
}
