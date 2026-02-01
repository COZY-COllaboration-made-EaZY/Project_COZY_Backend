package com.ohgiraffers.COZYbe.domain.task.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskListDTO(
        Long taskId,
        String title,
        String nickName,
        String status,
        String taskText,
        UUID userId,
        LocalDateTime createdAt
) {}
