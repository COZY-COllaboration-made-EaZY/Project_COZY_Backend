package com.ohgiraffers.COZYbe.domain.joinrequest.application.dto.response;

import java.time.LocalDateTime;

public record JoinRequestDTO(
        String requestId,
        String requesterName,
        String teamName,
        String message,
        LocalDateTime createdAt
) {
}
