package com.ohgiraffers.COZYbe.domain.personal.dto;

public record PersonalMemoCreateDTO(
        String title,
        String content,
        java.util.List<String> tags
) {
}
