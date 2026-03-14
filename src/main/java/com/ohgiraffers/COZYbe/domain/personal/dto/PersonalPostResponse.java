package com.ohgiraffers.COZYbe.domain.personal.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PersonalPostResponse(
        UUID postId,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
