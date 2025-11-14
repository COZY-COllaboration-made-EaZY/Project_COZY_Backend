package com.ohgiraffers.COZYbe.domain.member.application.service;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.member.application.dto.response.MemberListDTO;
import com.ohgiraffers.COZYbe.domain.member.domain.entity.Member;
import com.ohgiraffers.COZYbe.domain.member.domain.service.MemberDomainService;
import com.ohgiraffers.COZYbe.domain.teams.domain.service.TeamDomainService;
import com.ohgiraffers.COZYbe.domain.user.domain.service.UserDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MemberAppService {

    private final MemberDomainService domainService;

    private final TeamDomainService teamDomainService;
    private final UserDomainService userDomainService;

    private final MemberMapper mapper;


    public MemberListDTO getMemberList(String teamId) {
        if (!teamDomainService.isTeamExist(teamId)) {
            throw new ApplicationException(ErrorCode.NO_SUCH_TEAM);
        }
        List<Member> members = domainService.findByTeam(teamId);
        return new MemberListDTO(mapper.entityListToDto(members));
    }

    public void joinMember(String teamId, String userId) {
        domainService.joinMember(
                teamDomainService.getReference(teamId),
                userDomainService.getReference(userId)
        );
    }

    public void leaveMember(String teamId, String userId) {
        domainService.deleteMember(teamId,userId);
    }
}
