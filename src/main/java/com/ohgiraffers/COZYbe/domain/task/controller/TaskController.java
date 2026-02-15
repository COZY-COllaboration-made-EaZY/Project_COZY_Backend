package com.ohgiraffers.COZYbe.domain.task.controller;

import com.ohgiraffers.COZYbe.domain.task.dto.TaskCreateDTO;
import com.ohgiraffers.COZYbe.domain.task.dto.TaskDTO;
import com.ohgiraffers.COZYbe.domain.task.dto.TaskDetailDTO;
import com.ohgiraffers.COZYbe.domain.task.dto.TaskListDTO;
import com.ohgiraffers.COZYbe.domain.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //신규 테스트를 생성
    @PostMapping("/create")
    public ResponseEntity<TaskCreateDTO> createTask(
            @Valid @RequestBody TaskDTO createTaskDTO,
            @AuthenticationPrincipal Jwt userJwt
    ) {
        UUID userId = UUID.fromString(userJwt.getSubject());
        return ResponseEntity.ok(taskService.createTask(createTaskDTO, userId));
    }


    @GetMapping("/list")
    public ResponseEntity<List<TaskListDTO>> list(@RequestParam UUID projectId) {
        return ResponseEntity.ok(taskService.list(projectId));
    }

    @GetMapping("/detail")
    public ResponseEntity<TaskDetailDTO> detail(
            @RequestParam UUID projectId,
            @RequestParam Long taskId,
            @AuthenticationPrincipal Jwt userJwt
    ) {
        UUID userId = UUID.fromString(userJwt.getSubject());
        return ResponseEntity.ok(
                taskService.detail(projectId, taskId, userId)
        );
    }







}
