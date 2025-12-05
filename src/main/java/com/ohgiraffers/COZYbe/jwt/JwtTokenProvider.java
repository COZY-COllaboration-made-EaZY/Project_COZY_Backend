package com.ohgiraffers.COZYbe.jwt;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.auth.application.dto.AccessInfoDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Slf4j
@AllArgsConstructor
@Component
public class JwtTokenProvider {

    private final JwtProperties properties;

    private static final String REFRESH_COOKIE_NAME = "refreshToken";

    /**
     * 토큰생성
     * <pre>{@code
     * issuer : 발행자, 서버
     * sub : userId
     * audience : 토큰 수신자, 대상 애플리케이션
     * issuedAt : 발행일
     * exp : 만료일
     * claims : 추가 데이터
     * }</pre>
     *
     * @param userInfo AccessInfoDTO;
     * @return JWT Token
     * */
    public String createAccessToken(AccessInfoDTO userInfo) {
        Instant now = Instant.now();
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer("COZY")
                .subject(userInfo.userId().toString())
                .audience().add("COZY CLIENT").and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(properties.getAccessExpiration())))
                .claims(userInfo.toMap())
                .signWith(properties.getKey())
                .compact();
    }

    /**
     * Refresh Token은 많은 claim을 담을 필요 없음
     * */
    public String createRefreshToken(String userId, String jti, String deviceId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .id(jti)
                .issuer("COZY")
                .subject(userId)
                .audience().add("COZY CLIENT").and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(properties.getRefreshExpiration())))
                .signWith(properties.getKey())
                .claim("deviceId", deviceId)
                .compact();
    }

    public String createRefreshToken(String userId){
        return this.createRefreshToken(userId, UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    public ResponseCookie createRefreshCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(properties.getRefreshDuration())
                .build();
    }

    public ResponseCookie deleteRefreshCookie() {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)  //ttl을 0으로 해서 바로 삭제되게
                .build();
    }

    public String decodeJtiFromJwt(String token){
        return decodeJwt(token).getId();
    }

    public String decodeUserIdFromJwt(String token) {
        return decodeJwt(token).getSubject();
    }



    /**
     * 서명 검증되면 디코드 <br>
     * 검증 되는 목록 : 암호화, ttl
     * */
    public Claims decodeJwt(String token){
        if (token == null || token.trim().isEmpty()){
            throw new ApplicationException(ErrorCode.ANONYMOUS_USER);
        }
        try {
            return Jwts.parser()
                    .verifyWith(properties.getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch (Exception e){
            log.error(e.getMessage());
            throw new ApplicationException(ErrorCode.INVALID_TOKEN);
        }
    }
}
