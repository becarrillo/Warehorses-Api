package com.web.warehorses_secure_persistence.domain.warehouse.dto;

import com.web.warehorses_secure_persistence.domain.company.dto.CompanyResponseDto;
import com.web.warehorses_secure_persistence.domain.item.dto.ItemResponseDto;
import lombok.Builder;

import java.util.List;

@Builder
public record WarehouseResponseDto(
        short warehouseId,
        String address,
        CompanyResponseDto company,
        List<ItemResponseDto> items
) {
}
