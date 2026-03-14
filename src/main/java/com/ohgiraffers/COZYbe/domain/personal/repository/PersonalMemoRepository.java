package com.ohgiraffers.COZYbe.domain.personal.repository;

import com.ohgiraffers.COZYbe.domain.personal.entity.PersonalMemo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonalMemoRepository extends JpaRepository<PersonalMemo, UUID> {
    List<PersonalMemo> findAllByUser_UserIdOrderByCreatedAtDesc(UUID userId);
    Optional<PersonalMemo> findByMemoIdAndUser_UserId(UUID memoId, UUID userId);
}
