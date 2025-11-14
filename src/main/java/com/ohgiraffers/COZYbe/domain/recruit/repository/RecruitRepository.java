package com.ohgiraffers.COZYbe.domain.recruit.repository;

import com.ohgiraffers.COZYbe.domain.recruit.entity.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Long> {
}
