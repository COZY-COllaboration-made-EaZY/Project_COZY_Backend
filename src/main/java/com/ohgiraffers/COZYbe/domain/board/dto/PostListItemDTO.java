package com.ohgiraffers.COZYbe.domain.board.dto;

import java.time.LocalDateTime;

public record PostListItemDTO(
        String postId,
        String title,
        String authorName,
        long likeCount,
        LocalDateTime createdAt
) {}
