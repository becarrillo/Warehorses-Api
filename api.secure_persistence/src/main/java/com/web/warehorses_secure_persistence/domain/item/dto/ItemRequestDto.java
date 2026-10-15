package com.web.warehorses_secure_persistence.domain.item.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ItemRequestDto(
        String description,
        String supplier,
        BigDecimal unitPrice,
        BigDecimal salePrice,
        int quantity
) {
}
