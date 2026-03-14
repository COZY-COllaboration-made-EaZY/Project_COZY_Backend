package com.ohgiraffers.COZYbe.domain.user.application.dto;

public record UserInfoDTO(
        String userId,
        String email,
        String nickname,
        String profileImageUrl,
        String statusMessage,
        com.ohgiraffers.COZYbe.domain.user.domain.entity.UserRole role,
        String themeMode,
        Boolean notificationsEmail,
        Boolean notificationsPush,
        Boolean digestWeekly,
        Boolean profileVisible,
        String locale
) {
}
