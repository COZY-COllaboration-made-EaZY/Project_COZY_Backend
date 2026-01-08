package com.ohgiraffers.COZYbe.domain.recruit.repository;

import com.ohgiraffers.COZYbe.domain.recruit.entity.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Long> {

    @Query("""
        select r
        from Recruit r
        join fetch r.team
    """)
    List<Recruit> findAllWithTeam();
}
