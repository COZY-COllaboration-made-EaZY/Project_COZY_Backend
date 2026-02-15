package com.ohgiraffers.COZYbe.domain.task.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskCreateDTO(
        Long id,
        String title,
        String status,
        String taskText,
        String nickName,
        LocalDateTime createdAt
) {}

