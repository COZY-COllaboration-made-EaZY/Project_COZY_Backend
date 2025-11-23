package com.ohgiraffers.COZYbe.domain.auth.domain.entity;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash
public class RefreshToken {
    @Id
    private String id;

    private String userId;

    private String jti;

    private String deviceId;

    @TimeToLive     // default TimeUnit.SECONDS
    private Long ttl;

}
