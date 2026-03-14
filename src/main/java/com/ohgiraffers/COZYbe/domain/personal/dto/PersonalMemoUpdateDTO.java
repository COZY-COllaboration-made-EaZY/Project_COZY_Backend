package com.ohgiraffers.COZYbe.domain.personal.dto;

public record PersonalMemoUpdateDTO(
        String title,
        String content,
        java.util.List<String> tags
) {
}
