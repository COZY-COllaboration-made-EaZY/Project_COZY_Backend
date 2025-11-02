package com.ohgiraffers.COZYbe.domain.projects.controller;
import com.ohgiraffers.COZYbe.domain.projects.dto.CreateProjectDTO;
import com.ohgiraffers.COZYbe.domain.projects.dto.ProjectDetailResponse;
import com.ohgiraffers.COZYbe.domain.projects.entity.Project;
import com.ohgiraffers.COZYbe.domain.projects.service.ProjectService;
import com.ohgiraffers.COZYbe.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;

    @GetMapping("/check-project-name")
    public ResponseEntity<?> checkProjectName(@RequestParam String projectName) {
        System.out.println("projectName :: " + projectName);
        boolean available = projectService.isProjectNameAvailable(projectName);
        return ResponseEntity.ok(Map.of("available", available));
    }

    @PostMapping("/project-create")
    public ResponseEntity<?> createProject(@RequestBody CreateProjectDTO dto) {
        Project project = projectService.createProject(dto);
        return ResponseEntity.ok(Map.of(
                "id", project.getProjectId(),
                "projectName", project.getProjectName()
        ));
    }

    @GetMapping("/my-team-project-list")
    public ResponseEntity<?> getMyProjectInfo(@RequestParam UUID teamId) {
        List<Project> projects = projectService.getProjectsByTeamId(teamId);
        return ResponseEntity.ok(Map.of(
                "teamId", teamId,
                "hasProject", !projects.isEmpty(),
                "count", projects.size(),
                "projects", projects
        ));
    }

    @GetMapping("/project-detail-info")
    public ResponseEntity<ProjectDetailResponse> getProjectDetailInfo(@RequestParam UUID projectId) {
        System.out.println("projectId :: " + projectId);
        var p = projectService.getProjectByProjectId(projectId);
        System.out.println("projectName :: " + p.getProjectName());
        var body = new ProjectDetailResponse(
                p.getProjectId(), p.getProjectName(), p.getDescription(), p.getLeaderName(),
                p.getGitHubUrl(), p.getTeamId(), p.getCreatedAt(), p.getUpdatedDate()
        );
        return ResponseEntity.ok(body);
    }

//    @DeleteMapping("/{projectId}")
//    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId, HttpServletRequest request) {
//        String auth = request.getHeader("Authorization");
//        if (auth == null || !auth.startsWith("Bearer ")) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//        String token = auth.substring(7);
//        String userId = jwtTokenProvider.decodeUserIdFromJwt(token);
//
//        projectService.deleteProject(projectId, userId);
//        return ResponseEntity.noContent().build();
//    }


//    @PutMapping("/{projectId}")
//    public ResponseEntity<?> updateProject(@PathVariable Long projectId,
//                                           @RequestBody @Valid UpdateProjectDTO dto,
//                                           HttpServletRequest req) {
//        String userId = extractUserId(req);
//        Project p = projectService.updateProject(dto, projectId, userId);
//        return ResponseEntity.ok(Map.of(
//                "projectId", p.getProjectId(),
//                "projectName", p.getProjectName(),
//                "description", p.getDescription(),
//                "devInterest", p.getDevInterest(),
//                "githubUrl", p.getGitHubUrl(),
//                "ownerName", p.getLeaderName(),
//                "createdAt", p.getCreatedAt()
//        ));
//    }






}

