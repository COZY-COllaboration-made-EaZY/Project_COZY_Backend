package com.ohgiraffers.COZYbe.domain.board.repository;

import com.ohgiraffers.COZYbe.domain.board.entity.PostType;
import com.ohgiraffers.COZYbe.domain.board.entity.TeamPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TeamPostRepository extends JpaRepository<TeamPost, UUID> {
    List<TeamPost> findByTeam_TeamIdAndTypeOrderByCreatedAtDesc(UUID teamId, PostType type);
    long countByTeam_TeamIdAndType(UUID teamId, PostType type);
}
