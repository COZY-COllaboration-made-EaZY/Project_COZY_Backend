package com.ohgiraffers.COZYbe.domain.teams.application.dto.response;

import java.util.List;
import java.util.UUID;

public record TeamStatsDTO(
        UUID teamId,
        long projectCount,
        long noticeCount,
        long joinRequestCount,
        long upgradeRequestCount,
        long leaveRequestCount,
        long inactiveMemberCount,
        List<InactiveMemberDTO> inactiveMembers
) {
    public record InactiveMemberDTO(
            UUID userId,
            String nickname,
            String lastLoginAt
    ) {}
}
