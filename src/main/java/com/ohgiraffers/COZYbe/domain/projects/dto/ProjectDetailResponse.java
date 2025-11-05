package com.ohgiraffers.COZYbe.domain.projects.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Builder
public record ProjectDetailResponse(
        UUID projectId,
        String projectName,
        String devInterest,
        String description,
        String leaderName,
        String gitHubUrl,
        UUID teamId,
        UUID leaderId,
        UUID subLeaderId
) {}
