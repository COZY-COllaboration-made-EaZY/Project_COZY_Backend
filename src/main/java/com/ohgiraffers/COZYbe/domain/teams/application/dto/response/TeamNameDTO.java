package com.ohgiraffers.COZYbe.domain.teams.application.dto.response;


public record TeamNameDTO(
        String teamId,
        String teamName,
        String description,
        Integer memberCount,
        Integer projectCount
) {
}
