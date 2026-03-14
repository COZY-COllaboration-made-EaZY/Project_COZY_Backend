package com.ohgiraffers.COZYbe.domain.projects.service;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.projects.dto.CreateProjectDTO;
import com.ohgiraffers.COZYbe.domain.projects.dto.ProjectDetailResponse;
import com.ohgiraffers.COZYbe.domain.projects.dto.ProjectListItemResponse;
import com.ohgiraffers.COZYbe.domain.projects.dto.UpdateProjectDTO;
import com.ohgiraffers.COZYbe.domain.projects.dto.DeleteProjectDTO;
import com.ohgiraffers.COZYbe.domain.projects.entity.Project;
import com.ohgiraffers.COZYbe.domain.projects.repository.ProjectRepository;
import com.ohgiraffers.COZYbe.domain.member.domain.service.MemberDomainService;
import com.ohgiraffers.COZYbe.domain.teams.domain.entity.Team;
import com.ohgiraffers.COZYbe.domain.teams.domain.service.TeamDomainService;
import com.ohgiraffers.COZYbe.domain.user.application.service.UserAppService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TeamDomainService teamDomainService;
    private final UserAppService userAppService;
    private final MemberDomainService memberDomainService;

    // 공통 권한 검사
    private void assertLeaderOrSub(UUID teamId, UUID currentUserId){
        Team team = teamDomainService.getTeam(teamId);
        boolean isLeader = team.getLeader() != null && team.getLeader().getUserId().equals(currentUserId);
        boolean isSubLeader = team.getSubLeader() != null && team.getSubLeader().getUserId().equals(currentUserId);
        if (!(isLeader || isSubLeader)) {
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
        }
    }

    // 중복
    public boolean isProjectNameAvailable(String projectName) {
        return !projectRepository.existsByProjectName(projectName);
    }

    // 팀장만 프로젝트를 만들수 있게.
    @Transactional
    public Project createProject(CreateProjectDTO dto, UUID currentUserId) {
        String rawTeamId = dto.getTeamId();
        if (rawTeamId == null || rawTeamId.isBlank()) {
            throw new ApplicationException(ErrorCode.INVALID_TEAM_ID);
        }

        UUID teamId;
        try {
            teamId = UUID.fromString(rawTeamId);
        } catch (IllegalArgumentException ex) {
            throw new ApplicationException(ErrorCode.INVALID_TEAM_ID);
        }
        assertLeaderOrSub(teamId, currentUserId);

        Team team = teamDomainService.getTeam(teamId);
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
    public List<ProjectListItemResponse> getProjectsByTeamId(UUID teamId, UUID currentUserId) {
        if (!teamDomainService.isTeamExist(teamId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found: " + teamId);
        }
        if (!memberDomainService.isMemberOfTeam(teamId.toString(), currentUserId.toString())) {
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
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
    public ProjectDetailResponse getProjectDetailInfo(UUID projectId, UUID currentUserId) {
        Project p = projectRepository.findWithAllByProjectId(projectId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_PROJECT));
        Team team = p.getTeam();
        if (!memberDomainService.isMemberOfTeam(team.getTeamId().toString(), currentUserId.toString())) {
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
        }

        return ProjectDetailResponse.builder()
                .projectId(p.getProjectId())
                .projectName(p.getProjectName())
                .devInterest(p.getDevInterest())
                .description(p.getDescription())
                .gitHubUrl(p.getGitHubUrl())
                .teamId(team.getTeamId())
                .teamName(team.getTeamName())
                .leaderId(team.getLeader() != null ? team.getLeader().getUserId() : null)
                .subLeaderId(team.getSubLeader() != null ? team.getSubLeader().getUserId() : null)
                .leaderName(team.getLeader() != null ? team.getLeader().getNickname() : null)
                .build();
    }

    @Transactional
    public void deleteProject(UUID projectId, UUID currentUserId, DeleteProjectDTO deleteDTO) {
        Project p = projectRepository.findWithAllByProjectId(projectId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_PROJECT));
        assertLeaderOrSub(p.getTeam().getTeamId(), currentUserId);
        if (deleteDTO == null || deleteDTO.teamName() == null || deleteDTO.teamName().isBlank()
                || !deleteDTO.teamName().equals(p.getTeam().getTeamName())) {
            throw new ApplicationException(ErrorCode.INVALID_TEAM_NAME);
        }
        if (deleteDTO.password() == null || deleteDTO.password().isBlank()
                || !userAppService.verifyPassword(String.valueOf(currentUserId), deleteDTO.password())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }
        projectRepository.delete(p);
    }

    @Transactional
    public Project updateProject(UpdateProjectDTO dto, UUID projectId, UUID currentUserId) {
        Project p = projectRepository.findWithAllByProjectId(projectId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_PROJECT));
        assertLeaderOrSub(p.getTeam().getTeamId(), currentUserId);

        p.setProjectName(dto.getProjectName());
        p.setDevInterest(dto.getDevInterest());
        p.setDescription(dto.getDescription());
        p.setGitHubUrl(dto.getGitHubUrl());
        return p;
    }



}
