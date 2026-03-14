package com.ohgiraffers.COZYbe.domain.board.dto;

import java.time.LocalDateTime;

public record CommentDTO(
        String commentId,
        String authorName,
        String content,
        LocalDateTime createdAt
) {}
