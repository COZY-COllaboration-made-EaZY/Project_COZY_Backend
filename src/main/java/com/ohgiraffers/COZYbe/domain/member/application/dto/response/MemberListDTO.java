package com.ohgiraffers.COZYbe.domain.member.application.dto.response;

import java.util.List;

public record MemberListDTO(
        List<MemberBriefDTO> memberList
) {
}
