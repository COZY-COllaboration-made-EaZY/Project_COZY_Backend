package com.ohgiraffers.COZYbe.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;
import java.time.Duration;

@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private Duration accessTokenExpiration;
    private Duration refreshTokenExpiration;

    private String secret;

    public Long getAccessExpiration(){
        return accessTokenExpiration.toMillis();
    }

    public Long getRefreshExpiration(){
        return refreshTokenExpiration.toMillis();
    }

    public Duration getRefreshDuration(){
        return this.refreshTokenExpiration;
    }

    public SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());        //HMAC SHA-512 로 적용됨
    }

}
