package com.ohgiraffers.COZYbe.domain.teams.domain.repository;

import com.ohgiraffers.COZYbe.domain.joinrequest.domain.entity.RequestStatus;
import com.ohgiraffers.COZYbe.domain.teams.domain.entity.TeamLeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamLeaveRequestRepository extends JpaRepository<TeamLeaveRequest, UUID> {
    Optional<List<TeamLeaveRequest>> findByTeam_TeamId(UUID teamId);
    Optional<List<TeamLeaveRequest>> findByTeam_TeamIdAndStatus(UUID teamId, RequestStatus status);
    Optional<TeamLeaveRequest> findByTeam_TeamIdAndRequester_UserIdAndStatus(UUID teamId, UUID userId, RequestStatus status);
    long countByTeam_TeamIdAndStatus(UUID teamId, RequestStatus status);
}
