package com.web.warehorses_secure_persistence.domain.activity.dto;

import com.web.warehorses_secure_persistence.domain.activity.entity.Activity;
import com.web.warehorses_secure_persistence.domain.client.dto.ClientResponseDto;
import com.web.warehorses_secure_persistence.domain.item.dto.ItemResponseDto;
import com.web.warehorses_secure_persistence.domain.user.dto.UserDetailsDto;
import org.springframework.lang.Nullable;

import java.util.List;

public record ActivityRequestDto(Activity.TxnType type,
                                 List<ItemResponseDto> items,
                                 ClientResponseDto client,
                                 boolean isActive,
                                 UserDetailsDto owner,
                                 @Nullable Activity.EvidenceType evidenceType,
                                 @Nullable String evidenceUrl) {
}
