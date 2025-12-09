package com.ohgiraffers.COZYbe.domain.auth.application.controller;

import com.ohgiraffers.COZYbe.domain.auth.application.dto.TokenWrapperDTO;
import com.ohgiraffers.COZYbe.domain.auth.application.dto.AuthDTO;
import com.ohgiraffers.COZYbe.domain.auth.application.dto.LoginDTO;
import com.ohgiraffers.COZYbe.domain.auth.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            @RequestBody LoginDTO loginDTO
    ) {
        AuthDTO authTokenDTO = authService.login(loginDTO, refreshToken);
        if (authTokenDTO.refreshCookie() == null){  //이미 유효한 리프레시 토큰이 있는 경우 refresh만
            return ResponseEntity.ok().body(new TokenWrapperDTO(authTokenDTO.accessToken()));
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authTokenDTO.refreshCookie().toString())
                .body(new TokenWrapperDTO(authTokenDTO.accessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ){
        TokenWrapperDTO tokenDTO = authService.reissueAccessToken(refreshToken);
        return ResponseEntity.ok().body(tokenDTO);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        ResponseCookie deleteCookie= authService.logout(refreshToken);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteCookie.toString()).build();
    }





}
