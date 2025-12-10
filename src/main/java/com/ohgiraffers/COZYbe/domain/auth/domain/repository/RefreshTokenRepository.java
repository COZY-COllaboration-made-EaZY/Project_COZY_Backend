package com.ohgiraffers.COZYbe.domain.auth.domain.repository;

import com.ohgiraffers.COZYbe.domain.auth.domain.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByUserIdAndDeviceId(String userId, String deviceId);

    List<RefreshToken> findByUserId(String userId);
}

