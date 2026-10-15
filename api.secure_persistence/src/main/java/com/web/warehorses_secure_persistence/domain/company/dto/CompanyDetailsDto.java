package com.web.warehorses_secure_persistence.domain.company.dto;

import lombok.Builder;

@Builder
public record CompanyDetailsDto(String nit,
                                 String name,
                                 String email,
                                 String address,
                                 boolean isEnabled) {
}