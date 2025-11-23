package com.ohgiraffers.COZYbe.domain.auth.application.service;

import com.ohgiraffers.COZYbe.domain.auth.application.dto.AccessInfoDTO;
import com.ohgiraffers.COZYbe.domain.auth.application.dto.TokenWrapperDTO;
import com.ohgiraffers.COZYbe.domain.auth.application.dto.AuthDTO;
import com.ohgiraffers.COZYbe.domain.auth.application.dto.LoginDTO;
import com.ohgiraffers.COZYbe.domain.auth.domain.entity.RefreshToken;
import com.ohgiraffers.COZYbe.domain.auth.domain.service.RefreshTokenService;
import com.ohgiraffers.COZYbe.domain.user.application.service.UserAppService;
import com.ohgiraffers.COZYbe.jwt.JwtTokenProvider;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final RefreshTokenService refreshTokenService;

    private final UserAppService userAppService;
    private final JwtTokenProvider jwtTokenProvider;


    @Transactional
    public AuthDTO login(LoginDTO loginDTO, String refreshToken) {
        AccessInfoDTO accessDTO = userAppService.verifyUser(loginDTO);
        if (refreshToken != null && !refreshToken.trim().isEmpty()){     //리프레시 토큰이 있나
            Claims claims = jwtTokenProvider.decodeJwt(refreshToken);   //토큰이 유효한가
            String deviceId = claims.get("deviceId").toString();
            String jti = this.getRefreshToken(accessDTO.userId().toString(), deviceId);
            if (jti != null){
                return new AuthDTO(null, createAccessToken(accessDTO));     //이미 유효한 리프레시 토큰이 있는 경우 access토큰만 생성
            }
            // 해당하는 리프레시 토큰이 없으면(null) 빠져나옴
        }

        ResponseCookie refreshCookie = this.createRefreshToken(accessDTO.userId().toString());
        String accessToken = createAccessToken(accessDTO);
        return new AuthDTO(refreshCookie, accessToken);
    }

    private ResponseCookie createRefreshToken(String userId){
        String jti = UUID.randomUUID().toString();
        String refreshToken = jwtTokenProvider.createRefreshToken(userId, jti);
        ResponseCookie refreshCookie = jwtTokenProvider.createRefreshCookie(refreshToken);
        RefreshToken tokenEntity = RefreshToken.builder()
                .userId(userId)
                .jti(jti)
                .ttl(refreshCookie.getMaxAge().toSeconds())
                .build();
        refreshTokenService.create(tokenEntity);
        return refreshCookie;
    }

    private String createAccessToken(AccessInfoDTO accessDTO){
        return jwtTokenProvider.createAccessToken(accessDTO);
    }

    private String getRefreshToken(String userId, String deviceId){
        RefreshToken tokenEntity = refreshTokenService.findByUserIdAndDeviceId(userId,deviceId);
        if(tokenEntity == null){
            return null;
        }
        return tokenEntity.getJti();
    }

    @Transactional
    public TokenWrapperDTO reissueAccessToken(String refreshToken) {
        String userId = this.verifyRefreshToken(refreshToken);
        AccessInfoDTO accessInfoDTO = userAppService.verifyUser(userId);
        return new TokenWrapperDTO(this.createAccessToken(accessInfoDTO));
    }

    private String verifyRefreshToken(String refreshToken){
        Claims claims = jwtTokenProvider.decodeJwt(refreshToken);
        String userId = claims.getSubject();
        String jti = claims.getId();
        RefreshToken tokenEntity = refreshTokenService.findByUserIdAndTokenId(userId, jti);  //로그아웃 되어있으면 여기서 걸림
        return tokenEntity.getUserId();
    }

    @Transactional
    public void logout(String refreshToken) {
        Claims claims = jwtTokenProvider.decodeJwt(refreshToken);
        RefreshToken tokenEntity = refreshTokenService.findByTokenId(claims.getId());
        refreshTokenService.delete(tokenEntity);
    }


    @Deprecated
    public String getUserIdFromToken(String token) {
        return jwtTokenProvider.decodeUserIdFromJwt(token);
    }

}
