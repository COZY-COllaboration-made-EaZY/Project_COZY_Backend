package com.ohgiraffers.COZYbe.domain.help.dto;

public record CreateHelpDTO(
        String type,
        String title,
        String content,
        String status
) {
}
