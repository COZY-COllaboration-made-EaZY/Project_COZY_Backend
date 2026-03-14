package com.ohgiraffers.COZYbe.domain.personal.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PersonalMemoResponse(
        UUID memoId,
        String title,
        String content,
        java.util.List<String> tags,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
