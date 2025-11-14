package com.ohgiraffers.COZYbe.domain.auth.service;

import com.ohgiraffers.COZYbe.domain.auth.dto.AuthTokenDTO;
import com.ohgiraffers.COZYbe.domain.auth.dto.LoginDTO;
import com.ohgiraffers.COZYbe.domain.user.application.service.UserAppService;
import com.ohgiraffers.COZYbe.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserAppService userAppService;
    private final JwtTokenProvider jwtTokenProvider;


    public AuthTokenDTO login(LoginDTO loginDTO) {
        UUID userId = userAppService.verifyUser(loginDTO);

        String accessToken = jwtTokenProvider.createToken(userId);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId);
        return new AuthTokenDTO(
                accessToken,
//                jwtTokenProvider.getValidTime()
                refreshToken
        );
    }



    public String getUserIdFromToken(String token) {
        return jwtTokenProvider.decodeUserIdFromJwt(token);
    }
}
