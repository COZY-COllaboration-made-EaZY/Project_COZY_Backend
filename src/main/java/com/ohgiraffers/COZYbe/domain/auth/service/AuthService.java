package com.ohgiraffers.COZYbe.domain.auth.service;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.auth.dto.AccessInfoDTO;
import com.ohgiraffers.COZYbe.domain.auth.dto.TokenWrapperDTO;
import com.ohgiraffers.COZYbe.domain.auth.dto.AuthDTO;
import com.ohgiraffers.COZYbe.domain.auth.dto.LoginDTO;
import com.ohgiraffers.COZYbe.domain.auth.entity.RefreshToken;
import com.ohgiraffers.COZYbe.domain.auth.repository.RefreshTokenRepository;
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

    private final UserAppService userAppService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;


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
            // 해당하는 리프레시 토큰이 없으면 빠져나옴
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
                .token(jti)
                .ttl(refreshCookie.getMaxAge().toSeconds())
                .version(1L)
                .build();
        refreshTokenRepository.save(tokenEntity);
        return refreshCookie;
    }

    private String createAccessToken(AccessInfoDTO accessDTO){
        return jwtTokenProvider.createAccessToken(accessDTO);
    }

    private String getRefreshToken(String userId, String deviceId){
        RefreshToken tokenEntity = refreshTokenRepository.findByTokenAndDeviceId(userId,deviceId).orElse(null);
        if(tokenEntity == null){
            return null;
        }
        return tokenEntity.getToken();
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
        RefreshToken tokenEntity = refreshTokenRepository.findByUserIdAndToken(userId, jti)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_TOKEN));  //로그아웃 되어있으면 여기서 걸림
        if (tokenEntity.getVersion() != claims.get("version")) {
            throw new ApplicationException(ErrorCode.INVALID_TOKEN);
        }
        return userId;
    }

    @Transactional
    public void logout(String jti) {
        RefreshToken tokenEntity = refreshTokenRepository.findByToken(jti)
                .orElseThrow(()-> new ApplicationException(ErrorCode.INVALID_TOKEN));
        refreshTokenRepository.delete(tokenEntity);
    }


    /**
     * 버전 올리는 함수 <br>
     * 버전 올려야 하는 경우 : 비밀번호 재설정, 모든 기기에서 로그아웃 눌렀을때 <br>
     * 리프레시 토큰의 로테이션과 혼동하지 말것
    * */
    public void increaseTokenVersion(){

    }



    public String getUserIdFromToken(String token) {
        return jwtTokenProvider.decodeUserIdFromJwt(token);
    }

}
