package com.ohgiraffers.COZYbe.domain.personal.repository;

import com.ohgiraffers.COZYbe.domain.personal.entity.PersonalPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonalPostRepository extends JpaRepository<PersonalPost, UUID> {
    List<PersonalPost> findAllByUser_UserIdOrderByCreatedAtDesc(UUID userId);
    Optional<PersonalPost> findByPostIdAndUser_UserId(UUID postId, UUID userId);
}
