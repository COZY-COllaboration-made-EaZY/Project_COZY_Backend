package com.ohgiraffers.COZYbe.domain.member.domain.repository;

import com.ohgiraffers.COZYbe.domain.member.domain.entity.Member;
import com.ohgiraffers.COZYbe.domain.teams.domain.entity.Team;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<List<Member>> findByTeam_TeamId(UUID teamId);

    Optional<Member> findByTeam_TeamIdAndUser_UserId(UUID teamId, UUID userId);

    Optional<List<Member>> findByUser_UserId(UUID userId);

    Optional<List<Member>> findByUser(User user);

    Optional<List<Member>> findByTeam(Team team);

    @Query("SELECT m.team.teamId FROM Member m WHERE m.user.userId = :userId")
    List<UUID> findTeamIdsByUserId(@Param("userId") UUID userId);
}
