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

    public RefreshToken findByUserIdAndTokenId(String userId, String jti) {
        return repository.findByUserIdAndJti(userId,jti)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_TOKEN));
    }

    public RefreshToken findByTokenId(String jti) {
        return repository.findByJti(jti)
                .orElseThrow(()-> new ApplicationException(ErrorCode.INVALID_TOKEN));
    }

    public void delete(RefreshToken tokenEntity) {
        repository.delete(tokenEntity);
    }
}
