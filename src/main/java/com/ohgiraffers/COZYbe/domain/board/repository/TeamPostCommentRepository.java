package com.ohgiraffers.COZYbe.domain.board.repository;

import com.ohgiraffers.COZYbe.domain.board.entity.TeamPostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TeamPostCommentRepository extends JpaRepository<TeamPostComment, UUID> {
    List<TeamPostComment> findByPost_PostIdOrderByCreatedAtAsc(UUID postId);
    long countByPost_PostId(UUID postId);
    void deleteByPost_PostId(UUID postId);
}
