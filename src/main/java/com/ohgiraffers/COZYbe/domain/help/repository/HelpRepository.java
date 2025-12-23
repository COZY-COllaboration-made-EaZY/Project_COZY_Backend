package com.ohgiraffers.COZYbe.domain.help.repository;

import com.ohgiraffers.COZYbe.domain.help.entity.Help;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpRepository extends JpaRepository<Help,Long> {
    List<Help> findByType(String type, Sort sort);
}
