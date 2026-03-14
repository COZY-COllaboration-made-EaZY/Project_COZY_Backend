package com.ohgiraffers.COZYbe.domain.board.repository;

import com.ohgiraffers.COZYbe.domain.board.entity.TeamPostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeamPostLikeRepository extends JpaRepository<TeamPostLike, UUID> {
    long countByPost_PostId(UUID postId);
    boolean existsByPost_PostIdAndUser_UserId(UUID postId, UUID userId);
    void deleteByPost_PostIdAndUser_UserId(UUID postId, UUID userId);
    void deleteByPost_PostId(UUID postId);
}
