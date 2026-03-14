package com.ohgiraffers.COZYbe.domain.board.dto;

import com.ohgiraffers.COZYbe.domain.board.entity.PostType;

public record CreatePostDTO(
        String teamId,
        PostType type,
        String title,
        String content
) {}
