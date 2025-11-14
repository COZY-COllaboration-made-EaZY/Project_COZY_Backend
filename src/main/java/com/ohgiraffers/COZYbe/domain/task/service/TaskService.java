//package com.ohgiraffers.COZYbe.domain.task.service;
//
//import com.ohgiraffers.COZYbe.domain.projects.entity.Project;
//import com.ohgiraffers.COZYbe.domain.projects.repository.ProjectRepository;
//import com.ohgiraffers.COZYbe.domain.task.dto.TaskDTO;
//import com.ohgiraffers.COZYbe.domain.task.dto.TaskResponseDTO;
//import com.ohgiraffers.COZYbe.domain.task.entity.Task;
//import com.ohgiraffers.COZYbe.domain.task.repository.TaskRepository;
//import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
//import com.ohgiraffers.COZYbe.domain.user.domain.repository.UserRepository;
//import jakarta.transaction.Transactional;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.UUID;
//
//@Slf4j
//@Service
//public class TaskService {
//
//    private final TaskRepository taskRepository;
//    private final UserRepository userRepository;
//    private final ProjectRepository projectRepository;
//
//    public TaskService(TaskRepository taskRepository, UserRepository userRepository, ProjectRepository projectRepository) {
//        this.taskRepository = taskRepository;
//        this.userRepository = userRepository;
//        this.projectRepository = projectRepository;
//    }
//
//    @Transactional
//    public TaskResponseDTO createTask(TaskDTO dto, UUID userId) {
//        Project project = projectRepository.findById(dto.getProjectId())
//                .orElseThrow(() -> new RuntimeException("프로젝트가 없습니다."));
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("사용자가 없습니다."));
//
//        Task task = Task.builder()
//                .project(project)
//                .user(user)
//                .nickName(dto.getNickName())
//                .title(dto.getTitle())
//                .status(dto.getStatus())
//                .taskText(dto.getTaskText())
//                .build();
//
//        Task saved = taskRepository.save(task);
//        return TaskResponseDTO.from(saved);
//    }
//
//
//    @Transactional
//    public List<TaskResponseDTO> list(Long projectId) {
//        List<Task> tasks = taskRepository.findAllByProject_ProjectId(projectId);
//        System.out.println("tasks :: " + tasks);
//        if (tasks == null){
//            log.info("Not Info");
//        }
//        System.out.println("tasks :: " + tasks);
//        return taskRepository.findAllByProject_ProjectId(projectId)
//                .stream()
//                .map(TaskResponseDTO::from)
//                .toList();
//    }
//
//
//}
