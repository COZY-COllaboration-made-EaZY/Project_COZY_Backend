package com.ohgiraffers.COZYbe.domain.user.application.dto;

public record UserInfoDTO(
        String email,
        String nickname,
        String profileImageUrl,
        String statusMessage
) {
}
