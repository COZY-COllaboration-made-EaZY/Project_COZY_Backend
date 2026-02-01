package com.ohgiraffers.COZYbe.domain.task.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record TaskDetailDTO(
        Long taskId,
        UUID projectId,
        UUID userId,
        String title,
        String nickName,
        String status,
        String taskText,
        LocalDateTime createdAt
) {
}
