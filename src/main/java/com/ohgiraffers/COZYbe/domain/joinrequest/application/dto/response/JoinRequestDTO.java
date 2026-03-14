package com.ohgiraffers.COZYbe.domain.joinrequest.application.dto.response;

import java.time.LocalDateTime;
import com.ohgiraffers.COZYbe.domain.joinrequest.domain.entity.RequestStatus;

public record JoinRequestDTO(
        String requestId,
        String requesterName,
        String teamName,
        String message,
        LocalDateTime createdAt,
        RequestStatus status,
        String rejectReason
) {
}
