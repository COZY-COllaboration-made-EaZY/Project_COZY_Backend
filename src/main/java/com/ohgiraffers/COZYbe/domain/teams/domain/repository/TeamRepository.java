package com.ohgiraffers.COZYbe.domain.teams.domain.repository;

import com.ohgiraffers.COZYbe.domain.teams.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {

    //    @Query("SELECT t FROM Team t WHERE LOWER(t.teamName) LIKE LOWER(CONCAT('%' ,:keyword , '%'))")
//    Page<Team> searchByName(@Param("keyword") String searchKeyword, Pageable pageable);
    List<Team> findByTeamNameContainingIgnoreCase(String searchKeyword);


    Optional<List<Team>> findByLeaderUserId(UUID userId);

    boolean existsByTeamName(String teamName);
}
