package com.web.warehorses_secure_persistence.domain.client.dto;

import com.web.warehorses_secure_persistence.domain.activity.dto.ActivityResponseDto;

import java.util.List;

public record ClientResponseDto(int clientId,
                                String name,
                                String phoneNumber,
                                List<ActivityResponseDto> activities
) { }
