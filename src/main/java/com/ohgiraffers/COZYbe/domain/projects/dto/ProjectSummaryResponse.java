package com.ohgiraffers.COZYbe.domain.projects.dto;

import java.util.Date;
import java.util.UUID;

public record ProjectSummaryResponse(
        UUID projectId,
        String projectName,
        String description,
        String leaderName,     // 표시용
        String subLeaderName,  // 표시용
        Date createdAt
) {}
