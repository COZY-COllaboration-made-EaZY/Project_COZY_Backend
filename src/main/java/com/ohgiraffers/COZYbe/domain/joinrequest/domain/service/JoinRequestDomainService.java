package com.ohgiraffers.COZYbe.domain.joinrequest.domain.service;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.joinrequest.domain.entity.JoinRequest;
import com.ohgiraffers.COZYbe.domain.joinrequest.domain.entity.RequestStatus;
import com.ohgiraffers.COZYbe.domain.joinrequest.domain.repository.JoinRequestRepository;
import com.ohgiraffers.COZYbe.domain.teams.domain.entity.Team;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class JoinRequestDomainService {

    private JoinRequestRepository repository;

    // 가입 요청 생성
    public JoinRequest createJoinRequest(User requester, Team team, String message) {
        JoinRequest joinRequest = JoinRequest.builder()
                .requester(requester)
                .team(team)
                .status(RequestStatus.PENDING)
                .message(message)
                .build();
        return repository.save(joinRequest);
    }

    // 요청 조회
    public JoinRequest getJoinRequest(String requestId) {
        return getJoinRequest(UUID.fromString(requestId));
    }

    public JoinRequest getJoinRequest(UUID requestId) {
        return repository.findById(requestId)
                .orElseThrow(this::noJoinRequest);
    }

    // 사용자가 보낸 요청 목록 조회

    public List<JoinRequest> findByRequesterAndStatus(String userId, RequestStatus requestStatus) {
        return findByRequesterAndStatus(UUID.fromString(userId), requestStatus);
    }

    private List<JoinRequest> findByRequesterAndStatus(UUID userId, RequestStatus requestStatus) {
        return repository.findByRequester_UserIdAndStatus(userId, requestStatus)
                .orElseThrow(this::noJoinRequest);
    }

    public List<JoinRequest> findByRequester(String userId) {
        return findByRequester(UUID.fromString(userId));
    }

    public List<JoinRequest> findByRequester(UUID userId) {
        return repository.findByRequester_UserId(userId)
                .orElseThrow(this::noJoinRequest);
    }

    public List<JoinRequest> findByRequester(User requester) {
        return repository.findByRequester(requester)
                .orElseThrow(this::noJoinRequest);
    }


    // 팀에 온 요청 목록 조회
    public List<JoinRequest> findByTeam(String teamId) {
        return findByTeam(UUID.fromString(teamId));
    }

    public List<JoinRequest> findByTeam(UUID teamId) {
        return repository.findByTeam_TeamId(teamId)
                .orElseThrow(this::noJoinRequest);
    }

    public List<JoinRequest> findByTeam(Team team) {
        return repository.findByTeam(team)
                .orElseThrow(this::noJoinRequest);
    }

    // 팀의 특정 상태 요청 조회
    public List<JoinRequest> findByTeamAndStatus(UUID teamId, RequestStatus status) {
        return repository.findByTeam_TeamIdAndStatus(teamId, status)
                .orElseThrow(this::noJoinRequest);
    }

    // 중복 요청 체크
    public Optional<JoinRequest> findPendingRequest(UUID userId, UUID teamId) {
        return repository.findByRequester_UserIdAndTeam_TeamIdAndStatus(
                userId, teamId, RequestStatus.PENDING);
    }

    // 요청 상태 업데이트
    public JoinRequest updateStatus(JoinRequest joinRequest, RequestStatus status) {
        JoinRequest updated = joinRequest.toBuilder()
                .status(status)
                .build();
        return repository.save(updated);
    }

    // 요청 삭제
    public void deleteJoinRequest(JoinRequest joinRequest) {
        repository.delete(joinRequest);
    }

    public void deleteJoinRequest(String requestId) {
        repository.delete(getJoinRequest(requestId));
    }

    public void deleteJoinRequest(UUID requestId) {
        repository.delete(getJoinRequest(requestId));
    }

    // 예외 처리
    private ApplicationException noJoinRequest() {
        return new ApplicationException(ErrorCode.NO_SUCH_JOIN_REQUEST);
    }

}
