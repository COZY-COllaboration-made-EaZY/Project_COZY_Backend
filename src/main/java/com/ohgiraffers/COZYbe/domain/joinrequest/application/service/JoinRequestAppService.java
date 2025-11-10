package com.ohgiraffers.COZYbe.domain.joinrequest.application.service;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.joinrequest.application.dto.request.CreateJoinRequestDTO;
import com.ohgiraffers.COZYbe.domain.joinrequest.application.dto.response.JoinRequestDTO;
import com.ohgiraffers.COZYbe.domain.joinrequest.application.dto.response.JoinRequestListDTO;
import com.ohgiraffers.COZYbe.domain.joinrequest.domain.entity.JoinRequest;
import com.ohgiraffers.COZYbe.domain.joinrequest.domain.entity.RequestStatus;
import com.ohgiraffers.COZYbe.domain.joinrequest.domain.service.JoinRequestDomainService;
import com.ohgiraffers.COZYbe.domain.member.domain.repository.MemberRepository;
import com.ohgiraffers.COZYbe.domain.member.domain.service.MemberDomainService;
import com.ohgiraffers.COZYbe.domain.teams.domain.entity.Team;
import com.ohgiraffers.COZYbe.domain.teams.domain.service.TeamDomainService;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import com.ohgiraffers.COZYbe.domain.user.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class JoinRequestAppService {

    private final JoinRequestDomainService domainService;
    private final TeamDomainService teamDomainService;
    private final UserDomainService userDomainService;
    private final MemberDomainService memberDomainService;
    private final MemberRepository memberRepository;
    private final JoinRequestMapper mapper;

    /**
     * 가입 요청 생성
     */
    @Transactional
    public JoinRequestDTO createJoinRequest(CreateJoinRequestDTO dto, String userId) {
        UUID teamUUID = UUID.fromString(dto.teamId());
        UUID userUUID = UUID.fromString(userId);

        // 팀 존재 여부 확인
        if (!teamDomainService.isTeamExist(dto.teamId())) {
            throw new ApplicationException(ErrorCode.NO_SUCH_TEAM);
        }

        // 이미 팀 멤버인지 확인
        if (memberRepository.findByTeam_TeamIdAndUser_UserId(teamUUID, userUUID).isPresent()) {
            throw new ApplicationException(ErrorCode.ALREADY_TEAM_MEMBER);
        }

        // 이미 대기 중인 요청이 있는지 확인
        if (domainService.findPendingRequest(userUUID, teamUUID).isPresent()) {
            throw new ApplicationException(ErrorCode.DUPLICATE_JOIN_REQUEST);
        }

        User requester = userDomainService.getUser(userId);
        Team team = teamDomainService.getTeam(dto.teamId());

        JoinRequest created = domainService.createJoinRequest(requester, team, dto.message());
        log.info("가입 요청 생성됨: 사용자 {} -> 팀 {}", requester.getNickname(), team.getTeamName());

        return mapper.entityToDto(created);
    }

    /**
     * 내가 보낸 요청 목록 조회
     */
    public JoinRequestListDTO getMyRequests(String userId) {
        List<JoinRequest> requests = domainService.findByRequesterAndStatus(
                userId, RequestStatus.PENDING);
        log.info("유저가 보낸 가입 요청 수 조회 : {}", requests.size());
        List<JoinRequestDTO> dtoList = mapper.entityListToDto(requests);
        return new JoinRequestListDTO(dtoList);
    }

    /**
     * 팀에 온 요청 목록 조회 (리더/서브리더만)
     */
    public JoinRequestListDTO getTeamRequests(String teamId, String userId) {
        verifyLeaderOrSubLeader(teamId, userId);

        List<JoinRequest> requests = domainService.findByTeamAndStatus(
                UUID.fromString(teamId), RequestStatus.PENDING);
        log.info("팀이 받은 가입 요청 수 조회 : {}", requests.size());
        List<JoinRequestDTO> dtoList = mapper.entityListToDto(requests);
        return new JoinRequestListDTO(dtoList);
    }

    /**
     * 가입 요청 승인 (리더/서브리더만)
     */
    @Transactional
    public void approveRequest(String requestId, String userId) {
        JoinRequest joinRequest = domainService.getJoinRequest(requestId);

        // 리더/서브리더 권한 확인
        verifyLeaderOrSubLeader(joinRequest.getTeam().getTeamId().toString(), userId);

        // 이미 처리된 요청인지 확인
        if (joinRequest.getStatus() != RequestStatus.PENDING) {
            throw new ApplicationException(ErrorCode.REQUEST_ALREADY_PROCESSED);
        }

        // Member 생성
        memberDomainService.joinMember(joinRequest.getTeam(), joinRequest.getRequester());

        // 상태 업데이트
        domainService.updateStatus(joinRequest, RequestStatus.APPROVED);

        log.info("가입 요청 승인됨: {} -> {}",
                joinRequest.getRequester().getNickname(),
                joinRequest.getTeam().getTeamName());
    }

    /**
     * 가입 요청 거부 (리더/서브리더만)
     */
    @Transactional
    public void rejectRequest(String requestId, String userId) {
        JoinRequest joinRequest = domainService.getJoinRequest(requestId);

        // 리더/서브리더 권한 확인
        verifyLeaderOrSubLeader(joinRequest.getTeam().getTeamId().toString(), userId);

        // 이미 처리된 요청인지 확인
        if (joinRequest.getStatus() != RequestStatus.PENDING) {
            throw new ApplicationException(ErrorCode.REQUEST_ALREADY_PROCESSED);
        }

        // 상태 업데이트
        domainService.updateStatus(joinRequest, RequestStatus.REJECTED);

        log.info("가입 요청 거부됨: {} -> {}",
                joinRequest.getRequester().getNickname(),
                joinRequest.getTeam().getTeamName());
    }

    /**
     * 가입 요청 취소 (본인만)
     */
    @Transactional
    public void cancelRequest(String requestId, String userId) {

        JoinRequest joinRequest = domainService.getJoinRequest(requestId);

        // 본인의 요청인지 확인
        if (!joinRequest.getRequester().getUserId().toString().equals(userId)) {
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
        }

        // PENDING 상태인지 확인
        if (joinRequest.getStatus() != RequestStatus.PENDING) {
            throw new ApplicationException(ErrorCode.REQUEST_ALREADY_PROCESSED);
        }

        domainService.deleteJoinRequest(joinRequest);

        log.info("가입 요청 취소됨: {} -> {}",
                joinRequest.getRequester().getNickname(),
                joinRequest.getTeam().getTeamName());
    }

    // *********** Private Helper Methods ***********

    /**
     * 리더 또는 서브리더인지 확인
     */
    private void verifyLeaderOrSubLeader(String teamId, String userId) {
        Team team = teamDomainService.getTeam(teamId);

        boolean isLeader = team.getLeader().getUserId().toString().equals(userId);
        boolean isSubLeader = team.getSubLeader() != null &&
                             team.getSubLeader().getUserId().toString().equals(userId);

        if (!isLeader && !isSubLeader) {
            log.info("Join Request ({}) : 리더급 권한 거절", teamId);
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
        }

        log.info("Join Request ({}) : 리더급 권한 승인", teamId);
    }
}
