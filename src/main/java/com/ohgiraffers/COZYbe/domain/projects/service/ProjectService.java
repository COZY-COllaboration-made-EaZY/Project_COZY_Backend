package com.ohgiraffers.COZYbe.domain.projects.service;

import com.ohgiraffers.COZYbe.domain.projects.dto.CreateProjectDTO;
import com.ohgiraffers.COZYbe.domain.projects.dto.UpdateProjectDTO;
import com.ohgiraffers.COZYbe.domain.projects.entity.Project;
import com.ohgiraffers.COZYbe.domain.projects.repository.ProjectRepository;
import com.ohgiraffers.COZYbe.domain.teams.domain.entity.Team;
import com.ohgiraffers.COZYbe.domain.teams.domain.repository.TeamRepository;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import com.ohgiraffers.COZYbe.domain.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public Project getProjectByProjectId(UUID projectId) {
        return projectRepository.findByProjectId(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectId));
    }

    public boolean isProjectNameAvailable(String projectName) {
        return !projectRepository.existsByProjectName(projectName);
    }


    public Project createProject(CreateProjectDTO dto) {
        System.out.println("서비스dto ::" + dto);
        UUID teamIdUUID = UUID.fromString(dto.getTeamId());
        Project project = Project.builder()
                .projectName(dto.getProjectName())
                .devInterest(dto.getDevInterest())
                .description(dto.getDescription())
                .leaderName(dto.getLeaderName())
                .gitHubUrl(dto.getGithubUrl())
                .teamId(teamIdUUID)
                .build();
        return projectRepository.save(project);
    }


    public List<Project> getProjectsByTeamId(UUID teamId) {
        return projectRepository.findAllByTeamId(teamId);
    }

    public Project getProjectByNameForUser(String projectName, String userId) {
        Project project = projectRepository.findByProjectName(projectName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."));
        return project;
    }

    @Transactional
    public void deleteProject(Long projectId, String userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Not found with id: " + projectId));
        projectRepository.delete(project);
    }



    @Transactional
    public Project updateProject(UpdateProjectDTO dto, Long projectId, String userId) {
        Project p = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."));

        p.setProjectName(dto.getProjectName());
        p.setDevInterest(dto.getDevInterest());
        p.setDescription(dto.getDescription());
        p.setGitHubUrl(dto.getGitHubUrl());
        if (dto.getLeaderName() != null) p.setLeaderName(dto.getLeaderName());

        return projectRepository.save(p);
    }


    public Project getProjectDetailForUser(String projectName, String userId) {
        Project project = projectRepository.findByProjectName(projectName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."));
        return project;
    }



}
