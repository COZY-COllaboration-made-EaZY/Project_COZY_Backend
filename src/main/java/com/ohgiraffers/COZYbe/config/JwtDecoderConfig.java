package com.ohgiraffers.COZYbe.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.SecretKey;

@Configuration
public class JwtDecoderConfig {

    private final SecretKey secretKey;

    public JwtDecoderConfig(SecretKey jwtHmacKey){
        this.secretKey = jwtHmacKey;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder
                .withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS512)   //jwtHmacKey 와 알고리즘 일치시켜야함
                .build();
    }
}
