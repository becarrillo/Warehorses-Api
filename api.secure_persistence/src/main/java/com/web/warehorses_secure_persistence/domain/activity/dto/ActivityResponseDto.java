package com.web.warehorses_secure_persistence.domain.activity.dto;

import com.web.warehorses_secure_persistence.domain.activity.entity.Activity;
import com.web.warehorses_secure_persistence.domain.client.dto.ClientResponseDto;
import com.web.warehorses_secure_persistence.domain.item.dto.ItemResponseDto;
import com.web.warehorses_secure_persistence.domain.user.dto.UserDetailsDto;
import lombok.Builder;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ActivityResponseDto(long activityId,
                                  Activity.TxnType type,
                                  List<ItemResponseDto> items,
                                  ClientResponseDto client,
                                  boolean isActive,
                                  UserDetailsDto owner,
                                  @Nullable Activity.EvidenceType evidenceType,
                                  @Nullable String evidenceUrl,
                                  LocalDateTime createdAt,
                                  String createdAtIso) {
}
