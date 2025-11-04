package com.ohgiraffers.COZYbe.domain.projects.service;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.domain.projects.dto.CreateProjectDTO;
import com.ohgiraffers.COZYbe.domain.projects.dto.ProjectDetailResponse;
import com.ohgiraffers.COZYbe.domain.projects.dto.ProjectListItemResponse;
import com.ohgiraffers.COZYbe.domain.projects.dto.UpdateProjectDTO;
import com.ohgiraffers.COZYbe.domain.projects.entity.Project;
import com.ohgiraffers.COZYbe.domain.projects.repository.ProjectRepository;
import com.ohgiraffers.COZYbe.domain.teams.domain.entity.Team;
import com.ohgiraffers.COZYbe.domain.teams.domain.repository.TeamRepository;
import com.ohgiraffers.COZYbe.domain.teams.domain.service.TeamDomainService;
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

import static com.ohgiraffers.COZYbe.common.error.ErrorCode.ANONYMOUS_USER;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TeamDomainService teamDomainService;

    public Project getProjectByProjectId(UUID projectId) {
        return projectRepository.findByProjectId(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + projectId));
    }

    public boolean isProjectNameAvailable(String projectName) {
        return !projectRepository.existsByProjectName(projectName);
    }

    // 팀장만 프로젝트를 만들수 있게.
    @Transactional
    public Project createProject(CreateProjectDTO dto, UUID currentUserId, boolean allowSubLeaderAlso) {
        UUID teamId = UUID.fromString(dto.getTeamId());

        // 팀 조회
        Team team = teamDomainService.getTeam(teamId);

        // 권한 체크(팀장 or (부팀장 허용시) 부팀장)
        boolean isLeader = team.getLeader() != null && team.getLeader().getUserId().equals(currentUserId);
        boolean isSubLeader = team.getSubLeader() != null && team.getSubLeader().getUserId().equals(currentUserId);
        if (!(isLeader || (allowSubLeaderAlso && isSubLeader))) {
            throw new ApplicationException(ANONYMOUS_USER);
        }

        Project project = Project.builder()
                .projectName(dto.getProjectName())
                .devInterest(dto.getDevInterest())
                .description(dto.getDescription())
                .gitHubUrl(dto.getGithubUrl())
                .team(team)
                .build();

        return projectRepository.save(project);
    }

    // 팀의 아이디를 통하여 팀이 만든 프로젝트들을 전부 가져온다.
    public List<ProjectListItemResponse> getProjectsByTeamId(UUID teamId) {
        if (!teamDomainService.isTeamExist(teamId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found: " + teamId);
        }

        return projectRepository.findAllByTeam_TeamIdOrderByCreatedAtDesc(teamId).stream()
                .map(project -> new ProjectListItemResponse(
                        project.getProjectId(),
                        project.getProjectName(),
                        project.getDevInterest(),
                        project.getDescription()
                ))
                .toList();

    }

    // 프로젝트의 아이디를 통하여 프로젝트의 상세 정보를 전부 가져온다.
    public ProjectDetailResponse getProjectDetailInfo(UUID projectId) {
        Project p = projectRepository.findWithAllByProjectId(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."));

        return ProjectDetailResponse.builder()
                .projectId(p.getProjectId())
                .projectName(p.getProjectName())
                .devInterest(p.getDevInterest())
                .description(p.getDescription())
                .gitHubUrl(p.getGitHubUrl())
                .teamId(p.getTeam().getTeamId())
                .build();
    }

    @Transactional
    public void deleteProject(UUID projectId, UUID currentUserId, boolean allowSubLeaderAlso) {
        Project p = projectRepository.findByProjectId(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Not found with id: " + projectId));

        Team team = p.getTeam();
        boolean isLeader = team.getLeader() != null && team.getLeader().getUserId().equals(currentUserId);
        boolean isSubLeader = team.getSubLeader() != null && team.getSubLeader().getUserId().equals(currentUserId);

        if (!(isLeader || (allowSubLeaderAlso && isSubLeader))) {
            throw new AccessDeniedException("프로젝트 삭제 권한이 없습니다.");
        }

        projectRepository.delete(p);
    }

    @Transactional
    public Project updateProject(UpdateProjectDTO dto, UUID projectId, UUID currentUserId, boolean allowSubLeaderAlso) {
        Project p = projectRepository.findByProjectId(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."));

        Team team = p.getTeam();
        boolean isLeader = team.getLeader() != null && team.getLeader().getUserId().equals(currentUserId);
        boolean isSubLeader = team.getSubLeader() != null && team.getSubLeader().getUserId().equals(currentUserId);

        if (!(isLeader || (allowSubLeaderAlso && isSubLeader))) {
            throw new AccessDeniedException("프로젝트 수정 권한이 없습니다. (팀장/부팀장만 가능)");
        }

        p.setProjectName(dto.getProjectName());
        p.setDevInterest(dto.getDevInterest());
        p.setDescription(dto.getDescription());
        p.setGitHubUrl(dto.getGitHubUrl());

        return p;
    }



}
