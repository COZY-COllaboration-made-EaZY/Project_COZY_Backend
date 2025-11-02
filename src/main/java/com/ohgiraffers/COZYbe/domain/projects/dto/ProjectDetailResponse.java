package com.ohgiraffers.COZYbe.domain.projects.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectDetailResponse(
        UUID projectId,
        String projectName,
        String description,
        String leaderName,
        String githubUrl,
        UUID teamId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
