package com.ohgiraffers.COZYbe.domain.projects.repository;

import com.ohgiraffers.COZYbe.domain.projects.entity.Project;
import com.ohgiraffers.COZYbe.domain.teams.domain.entity.Team;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByProjectName(String projectName);
    boolean existsByProjectName(String projectName);

    List<Project> findAllByTeamId(UUID teamId);
    Optional<Project> findByProjectId(UUID projectId);
}

