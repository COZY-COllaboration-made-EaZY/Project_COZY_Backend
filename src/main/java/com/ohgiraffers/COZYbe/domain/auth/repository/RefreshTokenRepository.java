package com.ohgiraffers.COZYbe.domain.auth.repository;

import com.ohgiraffers.COZYbe.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.repository.cdi.RedisRepositoryBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByUserIdAndToken(String userId, String token);

    Optional<RefreshToken> findByToken(String jti);

    Optional<RefreshToken> findByTokenAndDeviceId(String userId, String deviceId);

}

