//package com.ohgiraffers.COZYbe.domain.task.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ohgiraffers.COZYbe.domain.task.dto.TaskDTO;
//import com.ohgiraffers.COZYbe.domain.task.dto.TaskResponseDTO;
//import com.ohgiraffers.COZYbe.domain.task.entity.Task;
//import com.ohgiraffers.COZYbe.domain.task.service.TaskService;
//import jakarta.validation.Valid;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.UUID;
//
//@Slf4j
//@RestController
//@RequestMapping("/api/task")
//public class TaskController {
//    private final TaskService taskService;
//
//    public TaskController(TaskService taskService) {
//        this.taskService = taskService;
//    }
//
//    //신규 테스트를 생성
//    @PostMapping("/create")
//    public ResponseEntity<TaskResponseDTO> createTask(
//            @Valid @RequestBody TaskDTO createTaskDTO,
//            @AuthenticationPrincipal Jwt userJwt){
//        UUID userId = UUID.fromString(userJwt.getSubject());
//        return ResponseEntity.ok(taskService.createTask(createTaskDTO, userId));
//    }
//
//
//    @GetMapping("/list")
//    public ResponseEntity<List<TaskResponseDTO>> list(@RequestParam Long projectId) {
//        System.out.println("받은 projectId :: " + projectId);
//        return ResponseEntity.ok(taskService.list(projectId));
//    }
//
//
//
//
//
//}
