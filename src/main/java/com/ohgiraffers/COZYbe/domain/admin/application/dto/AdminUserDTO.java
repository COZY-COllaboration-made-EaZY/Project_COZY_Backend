package com.ohgiraffers.COZYbe.domain.admin.application.dto;

import com.ohgiraffers.COZYbe.domain.user.domain.entity.UserRole;

import java.time.LocalDateTime;

public record AdminUserDTO(
        String userId,
        String email,
        String nickname,
        UserRole role,
        Boolean blocked,
        LocalDateTime lastLoginAt
) {
}
