package com.ohgiraffers.COZYbe.domain.task.dto;

import java.util.UUID;

public record TaskDTO(
        UUID projectId,
        String nickName,
        String title,
        String status,
        String taskText
) {}

