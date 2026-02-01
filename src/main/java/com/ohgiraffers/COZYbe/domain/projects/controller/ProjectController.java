package com.ohgiraffers.COZYbe.domain.projects.controller;
import com.ohgiraffers.COZYbe.domain.projects.dto.CreateProjectDTO;
import com.ohgiraffers.COZYbe.domain.projects.dto.ProjectDetailResponse;
import com.ohgiraffers.COZYbe.domain.projects.dto.ProjectListItemResponse;
import com.ohgiraffers.COZYbe.domain.projects.dto.UpdateProjectDTO;
import com.ohgiraffers.COZYbe.domain.projects.entity.Project;
import com.ohgiraffers.COZYbe.domain.projects.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    //프로젝트 중복확인
    @GetMapping("/check-project-name")
    public ResponseEntity<?> checkProjectName(@RequestParam String projectName) {
        System.out.println("projectName :: " + projectName);
        boolean available = projectService.isProjectNameAvailable(projectName);
        return ResponseEntity.ok(Map.of("available", available));
    }


    @PostMapping("/project-create")
    public ResponseEntity<?> createProject(
            @AuthenticationPrincipal Jwt jwt, @RequestBody CreateProjectDTO dto
    ) {
        UUID currentUserId = UUID.fromString(jwt.getSubject());
        System.out.println("currentUserId :: " + currentUserId);
        Project project = projectService.createProject(dto, currentUserId);
        log.info("프로젝트 제작 성공");
        return ResponseEntity.ok(Map.of(
                "id", project.getProjectId(),
                "projectName", project.getProjectName()
        ));
    }

    // 팀의 프로젝트 리스트
    @GetMapping("/my-team-project-list")
    public ResponseEntity<?> getMyProjectInfo(@RequestParam UUID teamId) {
        List<ProjectListItemResponse> projects = projectService.getProjectsByTeamId(teamId);
        return ResponseEntity.ok(Map.of(
                "teamId", teamId,
                "hasProject", !projects.isEmpty(),
                "count", projects.size(),
                "projects", projects
        ));
    }

    // 프로젝트 상세
    @GetMapping("/project-detail-info")
    public ResponseEntity<ProjectDetailResponse> getProjectDetailInfo(@RequestParam UUID projectId) {
        System.out.println("projectId :: " + projectId);
        ProjectDetailResponse dto = projectService.getProjectDetailInfo(projectId);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID projectId,
                                              @AuthenticationPrincipal Jwt jwt) {
        UUID currentUserId = UUID.fromString(jwt.getSubject());
        projectService.deleteProject(projectId, currentUserId);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable UUID projectId,
                                           @RequestBody UpdateProjectDTO dto,
                                           @AuthenticationPrincipal Jwt jwt) {
        UUID currentUserId = UUID.fromString(jwt.getSubject());
        Project p = projectService.updateProject(dto, projectId, currentUserId);
        return ResponseEntity.ok(Map.of(
                "projectId", p.getProjectId(),
                "projectName", p.getProjectName(),
                "description", p.getDescription(),
                "devInterest", p.getDevInterest(),
                "githubUrl", p.getGitHubUrl(),
                "createdAt", p.getCreatedAt()
        ));
    }






}

