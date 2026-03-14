package com.ohgiraffers.COZYbe.domain.teams.application.service;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.joinrequest.domain.entity.RequestStatus;
import com.ohgiraffers.COZYbe.domain.member.domain.service.MemberDomainService;
import com.ohgiraffers.COZYbe.domain.teams.application.dto.request.RequestDecisionDTO;
import com.ohgiraffers.COZYbe.domain.teams.application.dto.request.TeamLeaveRequestDTO;
import com.ohgiraffers.COZYbe.domain.teams.application.dto.request.TeamUpgradeRequestDTO;
import com.ohgiraffers.COZYbe.domain.teams.application.dto.response.TeamStatsDTO;
import com.ohgiraffers.COZYbe.domain.teams.domain.entity.Team;
import com.ohgiraffers.COZYbe.domain.teams.domain.entity.TeamLeaveRequest;
import com.ohgiraffers.COZYbe.domain.teams.domain.entity.TeamRoleUpgradeRequest;
import com.ohgiraffers.COZYbe.domain.teams.domain.repository.TeamLeaveRequestRepository;
import com.ohgiraffers.COZYbe.domain.teams.domain.repository.TeamRoleUpgradeRequestRepository;
import com.ohgiraffers.COZYbe.domain.teams.domain.service.TeamDomainService;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import com.ohgiraffers.COZYbe.domain.user.domain.service.UserDomainService;
import com.ohgiraffers.COZYbe.domain.joinrequest.domain.repository.JoinRequestRepository;
import com.ohgiraffers.COZYbe.domain.board.repository.TeamPostRepository;
import com.ohgiraffers.COZYbe.domain.board.entity.PostType;
import com.ohgiraffers.COZYbe.domain.projects.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamRequestService {

    private final TeamDomainService teamDomainService;
    private final UserDomainService userDomainService;
    private final MemberDomainService memberDomainService;
    private final TeamRoleUpgradeRequestRepository upgradeRepository;
    private final TeamLeaveRequestRepository leaveRepository;

    private final ProjectRepository projectRepository;
    private final TeamPostRepository postRepository;
    private final JoinRequestRepository joinRequestRepository;

    private void assertLeader(Team team, String userId) {
        if (team.getLeader() == null || !team.getLeader().getUserId().toString().equals(userId)) {
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
        }
    }

    private Team getTeamOrThrow(String teamId) {
        return teamDomainService.getTeam(teamId);
    }

    private void assertMember(String teamId, String userId) {
        if (!memberDomainService.isMemberOfTeam(teamId, userId)) {
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
        }
    }

    // ===== 승급 요청 =====
    @Transactional
    public void requestUpgrade(TeamUpgradeRequestDTO dto, String userId) {
        String teamId = dto.teamId();
        assertMember(teamId, userId);
        UUID teamUUID = UUID.fromString(teamId);
        UUID userUUID = UUID.fromString(userId);
        upgradeRepository.findByTeam_TeamIdAndRequester_UserIdAndStatus(teamUUID, userUUID, RequestStatus.PENDING)
                .ifPresent(req -> { throw new ApplicationException(ErrorCode.DUPLICATE_REQUEST); });
        Team team = getTeamOrThrow(teamId);
        User requester = userDomainService.getReference(userId);
        TeamRoleUpgradeRequest req = TeamRoleUpgradeRequest.builder()
                .team(team)
                .requester(requester)
                .status(RequestStatus.PENDING)
                .message(dto.message())
                .build();
        upgradeRepository.save(req);
    }

    public List<TeamRoleUpgradeRequest> getUpgradeRequests(String teamId, String userId) {
        Team team = getTeamOrThrow(teamId);
        assertLeader(team, userId);
        return upgradeRepository.findByTeam_TeamId(UUID.fromString(teamId)).orElse(List.of());
    }

    @Transactional
    public void approveUpgrade(UUID requestId, String userId) {
        TeamRoleUpgradeRequest req = upgradeRepository.findById(requestId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_UPGRADE_REQUEST));
        assertLeader(req.getTeam(), userId);
        req.setStatus(RequestStatus.APPROVED);
        req.getTeam().setSubLeader(req.getRequester());
        teamDomainService.saveTeam(req.getTeam());
    }

    @Transactional
    public void rejectUpgrade(UUID requestId, String userId, RequestDecisionDTO dto) {
        TeamRoleUpgradeRequest req = upgradeRepository.findById(requestId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_UPGRADE_REQUEST));
        assertLeader(req.getTeam(), userId);
        req.setStatus(RequestStatus.REJECTED);
        if (dto != null) {
            req.setMessage(dto.message());
        }
    }

    // ===== 탈퇴 요청 =====
    @Transactional
    public void requestLeave(TeamLeaveRequestDTO dto, String userId) {
        String teamId = dto.teamId();
        assertMember(teamId, userId);
        UUID teamUUID = UUID.fromString(teamId);
        UUID userUUID = UUID.fromString(userId);
        leaveRepository.findByTeam_TeamIdAndRequester_UserIdAndStatus(teamUUID, userUUID, RequestStatus.PENDING)
                .ifPresent(req -> { throw new ApplicationException(ErrorCode.DUPLICATE_REQUEST); });
        Team team = getTeamOrThrow(teamId);
        User requester = userDomainService.getReference(userId);
        TeamLeaveRequest req = TeamLeaveRequest.builder()
                .team(team)
                .requester(requester)
                .status(RequestStatus.PENDING)
                .message(dto.message())
                .build();
        leaveRepository.save(req);
    }

    public List<TeamLeaveRequest> getLeaveRequests(String teamId, String userId) {
        Team team = getTeamOrThrow(teamId);
        assertLeader(team, userId);
        return leaveRepository.findByTeam_TeamId(UUID.fromString(teamId)).orElse(List.of());
    }

    @Transactional
    public void approveLeave(UUID requestId, String userId) {
        TeamLeaveRequest req = leaveRepository.findById(requestId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_LEAVE_REQUEST));
        assertLeader(req.getTeam(), userId);
        req.setStatus(RequestStatus.APPROVED);
        memberDomainService.deleteMember(req.getTeam().getTeamId().toString(), req.getRequester().getUserId().toString());
    }

    @Transactional
    public void rejectLeave(UUID requestId, String userId, RequestDecisionDTO dto) {
        TeamLeaveRequest req = leaveRepository.findById(requestId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_LEAVE_REQUEST));
        assertLeader(req.getTeam(), userId);
        req.setStatus(RequestStatus.REJECTED);
        if (dto != null) {
            req.setMessage(dto.message());
        }
    }

    // ===== 통계 =====
    public TeamStatsDTO getTeamStats(String teamId, String userId) {
        assertMember(teamId, userId);
        UUID teamUUID = UUID.fromString(teamId);
        long projectCount = projectRepository.countByTeam_TeamId(teamUUID);
        long noticeCount = postRepository.countByTeam_TeamIdAndType(teamUUID, PostType.NOTICE);
        long joinCount = joinRequestRepository.countByTeam_TeamIdAndStatus(teamUUID, RequestStatus.PENDING);
        long upgradeCount = upgradeRepository.countByTeam_TeamIdAndStatus(teamUUID, RequestStatus.PENDING);
        long leaveCount = leaveRepository.countByTeam_TeamIdAndStatus(teamUUID, RequestStatus.PENDING);

        LocalDateTime cutoff = LocalDateTime.now().minusDays(2);
        List<TeamStatsDTO.InactiveMemberDTO> inactiveMembers = memberDomainService.findByTeam(teamId).stream()
                .map(m -> m.getUser())
                .filter(u -> u.getLastLoginAt() == null || u.getLastLoginAt().isBefore(cutoff))
                .map(u -> new TeamStatsDTO.InactiveMemberDTO(
                        u.getUserId(),
                        u.getNickname(),
                        u.getLastLoginAt() != null ? u.getLastLoginAt().toString() : null
                ))
                .toList();

        return new TeamStatsDTO(
                teamUUID,
                projectCount,
                noticeCount,
                joinCount,
                upgradeCount,
                leaveCount,
                inactiveMembers.size(),
                inactiveMembers
        );
    }
}
