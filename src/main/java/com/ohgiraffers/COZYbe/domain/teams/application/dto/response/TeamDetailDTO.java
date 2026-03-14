package com.ohgiraffers.COZYbe.domain.teams.application.dto.response;



public record TeamDetailDTO(
        String teamId,
        String teamName,
        String description,
        String leaderName,
        String leaderId,
        String subLeaderId
) {
}
