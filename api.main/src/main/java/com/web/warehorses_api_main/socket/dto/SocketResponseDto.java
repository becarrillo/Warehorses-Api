package com.web.warehorses_api_main.socket.dto;

import lombok.Builder;

import java.util.UUID;


@Builder
public record SocketResponseDto(
        UUID sessionId,
        Object dto
) {}
