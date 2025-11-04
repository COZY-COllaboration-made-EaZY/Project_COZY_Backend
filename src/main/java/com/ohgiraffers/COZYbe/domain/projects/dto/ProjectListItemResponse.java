package com.ohgiraffers.COZYbe.domain.projects.dto;

import java.util.UUID;

public record ProjectListItemResponse(
        UUID projectId,
        String projectName,
        String devInterest,
        String description
) {}
