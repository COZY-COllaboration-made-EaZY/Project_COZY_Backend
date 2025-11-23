package com.ohgiraffers.COZYbe.domain.auth.application.dto;

import org.springframework.http.ResponseCookie;

public record AuthDTO(
        ResponseCookie refreshCookie,
        String accessToken
) {
}
