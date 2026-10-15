package com.web.warehorses_secure_persistence.domain.item.dto;

import com.web.warehorses_secure_persistence.domain.activity.dto.ActivityResponseDto;
import jdk.jfr.Unsigned;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ItemResponseDto(@Unsigned int itemId,
                              String description,
                              BigDecimal unitPrice,
                              BigDecimal salePrice,
                              int quantity,
                              String supplier,
                              List<ActivityResponseDto> activities) { }
