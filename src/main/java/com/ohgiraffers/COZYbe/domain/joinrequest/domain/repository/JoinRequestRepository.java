package com.ohgiraffers.COZYbe.domain.joinrequest.domain.repository;

import com.ohgiraffers.COZYbe.domain.joinrequest.domain.entity.JoinRequest;
import com.ohgiraffers.COZYbe.domain.joinrequest.domain.entity.RequestStatus;
import com.ohgiraffers.COZYbe.domain.teams.domain.entity.Team;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequest, UUID> {

    // 사용자가 보낸 모든 요청 조회
    Optional<List<JoinRequest>> findByRequester_UserId(UUID userId);

    // 팀에 온 모든 요청 조회
    Optional<List<JoinRequest>> findByTeam_TeamId(UUID teamId);

    // 팀에 온 특정 상태의 요청 조회
    Optional<List<JoinRequest>> findByTeam_TeamIdAndStatus(UUID teamId, RequestStatus status);

    // 사용자가 특정 팀에 보낸 특정 상태의 요청 조회 (중복 체크용)
    Optional<JoinRequest> findByRequester_UserIdAndTeam_TeamIdAndStatus(UUID userId, UUID teamId, RequestStatus status);

    // User로 조회
    Optional<List<JoinRequest>> findByRequester(User requester);

    // Team으로 조회
    Optional<List<JoinRequest>> findByTeam(Team team);

    Optional<List<JoinRequest>> findByRequester_UserIdAndStatus(UUID userId, RequestStatus requestStatus);
}
