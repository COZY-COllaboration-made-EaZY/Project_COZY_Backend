package com.ohgiraffers.COZYbe.domain.auth.application.dto;

import java.util.Map;
import java.util.UUID;

public record AccessInfoDTO(
        UUID userId,
        String email,
        String nickname
) {
    public Map<String, Object> toMap(){
        return Map.of(
                "userId", userId.toString(),
                "email", email,
                "nickname",nickname
        );
    }

    public static AccessInfoDTO fromMap(Map<String, Object> map){
        return new AccessInfoDTO(
                UUID.fromString((String) map.get("userId")),
                (String) map.get("email"),
                (String) map.get("nickname")
        );
    }
}
