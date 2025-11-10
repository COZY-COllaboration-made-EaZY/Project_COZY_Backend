package com.ohgiraffers.COZYbe.domain.auth.controller;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.auth.dto.AuthTokenDTO;
import com.ohgiraffers.COZYbe.domain.auth.dto.LoginDTO;
import com.ohgiraffers.COZYbe.domain.auth.service.AuthService;
import com.ohgiraffers.COZYbe.domain.auth.service.BlocklistService;
import com.ohgiraffers.COZYbe.domain.user.domain.service.UserDomainService;
import com.ohgiraffers.COZYbe.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final BlocklistService blocklistService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDomainService userDomainService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        AuthTokenDTO authTokenDTO = authService.login(loginDTO);
        String nickName = userDomainService.getUser(
                jwtTokenProvider.decodeUserIdFromJwt(authTokenDTO.accessToken())
        ).getNickname();
        log.info("user 로그인 : {}", nickName);
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", authTokenDTO.refreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", refreshCookie.toString())
                .body(Map.of(
                        "accessToken", authTokenDTO.accessToken()
                ));
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal Jwt jwt) {
        String jti = jwt.getId();

        long ttl = jwt.getExpiresAt().toEpochMilli() - System.currentTimeMillis();
        blocklistService.store(jti, ttl);
        return ResponseEntity.ok().build();
    }

}
