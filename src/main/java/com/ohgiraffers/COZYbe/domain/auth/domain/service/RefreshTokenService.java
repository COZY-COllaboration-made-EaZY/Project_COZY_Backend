package com.ohgiraffers.COZYbe.domain.auth.domain.service;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.auth.domain.entity.RefreshToken;
import com.ohgiraffers.COZYbe.domain.auth.domain.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository repository;


    public void create(RefreshToken tokenEntity) {
        repository.save(tokenEntity);
    }

    public RefreshToken findByUserIdAndDeviceId(String userId, String deviceId) {
        return repository.findByUserIdAndDeviceId(userId, deviceId)
                .orElse(null);
    }

    public RefreshToken findByUserIdAndTokenId(String jti, String userId) {
        return repository.findById(jti)
                .filter(token -> userId.equals(token.getUserId()))
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_TOKEN));
    }

    public RefreshToken findByTokenId(String jti) {
        return repository.findById(jti)
                .orElseThrow(()-> new ApplicationException(ErrorCode.INVALID_TOKEN));
    }

    public void delete(RefreshToken tokenEntity) {
        repository.delete(tokenEntity);
    }
}
