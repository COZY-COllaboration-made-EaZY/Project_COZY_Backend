package com.ohgiraffers.COZYbe.domain.board.dto;

import java.time.LocalDateTime;

public record PostDetailDTO(
        String postId,
        String title,
        String content,
        String authorName,
        long likeCount,
        long commentCount,
        boolean liked,
        LocalDateTime createdAt
) {}
