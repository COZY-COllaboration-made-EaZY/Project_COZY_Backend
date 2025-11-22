package com.ohgiraffers.COZYbe.domain.auth.entity;


import jakarta.persistence.Column;
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
    private String token;

    private String deviceId;
    private Long version;

    @TimeToLive
    private Long ttl;

}
