package com.ohgiraffers.COZYbe.domain.teams.domain.repository;

import com.ohgiraffers.COZYbe.domain.joinrequest.domain.entity.RequestStatus;
import com.ohgiraffers.COZYbe.domain.teams.domain.entity.TeamRoleUpgradeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamRoleUpgradeRequestRepository extends JpaRepository<TeamRoleUpgradeRequest, UUID> {
    Optional<List<TeamRoleUpgradeRequest>> findByTeam_TeamId(UUID teamId);
    Optional<List<TeamRoleUpgradeRequest>> findByTeam_TeamIdAndStatus(UUID teamId, RequestStatus status);
    Optional<TeamRoleUpgradeRequest> findByTeam_TeamIdAndRequester_UserIdAndStatus(UUID teamId, UUID userId, RequestStatus status);
    long countByTeam_TeamIdAndStatus(UUID teamId, RequestStatus status);
}
