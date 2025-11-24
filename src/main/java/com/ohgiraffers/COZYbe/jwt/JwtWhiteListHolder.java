package com.ohgiraffers.COZYbe.jwt;

import org.springframework.stereotype.Component;

@Component
public class JwtWhiteListHolder {

    private static final String[] WHITE_LIST = {
            // 실제 회원가입/로그인 API
            "/api/user/signup",
            "/api/user/register",
            "/api/user/check-email",

            // 기존 auth 경로 (로그인 관련)
            "/api/auth/login",
            "/api/auth/refresh",
            "/api/auth/logout",

            // Swagger
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",

            // Plan & Inquiry
            "/api/inquiries/list",
            "/api/plan/list",
            "/api/plan/{id}",
            "/api/plan/by-nickname"
    };

    public String[] getWhiteList(){
        return WHITE_LIST;
    }
}


