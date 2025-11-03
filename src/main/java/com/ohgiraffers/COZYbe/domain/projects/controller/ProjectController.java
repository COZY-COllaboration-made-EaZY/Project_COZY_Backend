package com.ohgiraffers.COZYbe.domain.projects.controller;
import com.ohgiraffers.COZYbe.domain.projects.dto.CreateProjectDTO;
import com.ohgiraffers.COZYbe.domain.projects.dto.ProjectDetailResponse;
import com.ohgiraffers.COZYbe.domain.projects.dto.ProjectListItemResponse;
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
    private final JwtTokenProvider jwtTokenProvider;

    //중복확인
    @GetMapping("/check-project-name")
    public ResponseEntity<?> checkProjectName(@RequestParam String projectName) {
        System.out.println("projectName :: " + projectName);
        boolean available = projectService.isProjectNameAvailable(projectName);
        return ResponseEntity.ok(Map.of("available", available));
    }


    @PostMapping("/project-create")
    public ResponseEntity<?> createProject(
            @RequestHeader("Authorization") String authorization,
            @RequestBody CreateProjectDTO dto
    ) {
        System.out.println("dto :: " + dto.toString());
        String token = jwtTokenProvider.extractToken(authorization);
        UUID currentUserId = UUID.fromString(jwtTokenProvider.decodeUserIdFromJwt(token));

        Project project = projectService.createProject(dto, currentUserId, false);
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
        ProjectDetailResponse dto = projectService.getProjectDetailInfo(projectId);
        return ResponseEntity.ok(dto);
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

