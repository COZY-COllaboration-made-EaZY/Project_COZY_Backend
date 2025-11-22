package com.ohgiraffers.COZYbe.domain.auth.dto;

import org.springframework.http.ResponseCookie;

public record AuthDTO(
        ResponseCookie refreshCookie,
        String accessToken
) {
}
