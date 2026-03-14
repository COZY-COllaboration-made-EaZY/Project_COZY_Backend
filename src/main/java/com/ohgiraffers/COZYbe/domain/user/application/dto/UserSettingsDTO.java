package com.ohgiraffers.COZYbe.domain.user.application.dto;

public record UserSettingsDTO(
        String themeMode,
        Boolean notificationsEmail,
        Boolean notificationsPush,
        Boolean digestWeekly,
        Boolean profileVisible,
        String locale
) {
}
