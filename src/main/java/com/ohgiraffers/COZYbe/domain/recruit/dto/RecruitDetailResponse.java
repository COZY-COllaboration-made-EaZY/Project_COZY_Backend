package com.ohgiraffers.COZYbe.domain.recruit.dto;

import java.time.LocalDateTime;

public record RecruitDetailResponse(
        Long id,
        String title,
        String recruitText,
        String nickName,
        String teamId,
        String teamName,
        LocalDateTime createdAt
) {}
