package com.ohgiraffers.COZYbe.domain.task.service;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.member.domain.repository.MemberRepository;
import com.ohgiraffers.COZYbe.domain.member.domain.service.MemberDomainService;
import com.ohgiraffers.COZYbe.domain.projects.entity.Project;
import com.ohgiraffers.COZYbe.domain.projects.repository.ProjectRepository;
import com.ohgiraffers.COZYbe.domain.task.dto.TaskDTO;
import com.ohgiraffers.COZYbe.domain.task.dto.TaskCreateDTO;
import com.ohgiraffers.COZYbe.domain.task.dto.TaskDetailDTO;
import com.ohgiraffers.COZYbe.domain.task.dto.TaskListDTO;
import com.ohgiraffers.COZYbe.domain.task.entity.Task;
import com.ohgiraffers.COZYbe.domain.task.repository.TaskRepository;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import com.ohgiraffers.COZYbe.domain.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final MemberDomainService memberDomainService;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, ProjectRepository projectRepository
        , MemberRepository memberRepository, MemberDomainService memberDomainService
    ) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
        this.memberDomainService = memberDomainService;
    }

    @Transactional
    public TaskCreateDTO createTask(TaskDTO dto, UUID userId) {
        Project project = projectRepository.findById(dto.projectId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_PROJECT));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_USER));

        Task savedTask = taskRepository.save(
                Task.builder()
                        .project(project)
                        .user(user)
                        .nickName(dto.nickName())
                        .title(dto.title())
                        .status(dto.status())
                        .taskText(dto.taskText())
                        .build()
        );

        return TaskCreateDTO.builder()
                .id(savedTask.getTaskId())
                .title(savedTask.getTitle())
                .status(savedTask.getStatus())
                .taskText(savedTask.getTaskText())
                .nickName(savedTask.getNickName())
                .createdAt(savedTask.getCreatedAt())
                .build();
    }



    @Transactional
    public List<TaskListDTO> list(UUID projectId) {

        List<Task> tasks = taskRepository.findAllByProject_ProjectId(projectId);

        if (tasks.isEmpty()) {
            log.info("Task 없음 - projectId={}", projectId);
            return List.of();
        }

        return tasks.stream()
                .map(task -> new TaskListDTO(
                        task.getTaskId(),
                        task.getTitle(),
                        task.getNickName(),
                        task.getStatus(),
                        task.getTaskText(),
                        task.getUser().getUserId(),
                        task.getCreatedAt()
                ))
                .toList();
    }

    @Transactional
    public TaskDetailDTO detail(UUID projectId, Long taskId, UUID userId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_TASK));

        if (!task.getProject().getProjectId().equals(projectId)) {
            throw new ApplicationException(ErrorCode.NO_SUCH_MEMBER);
        }

        UUID teamId = task.getProject().getTeam().getTeamId();
        memberDomainService.getMember(teamId.toString(), userId.toString());

        return TaskDetailDTO.builder()
                .taskId(task.getTaskId())
                .projectId(projectId)
                .userId(task.getUser().getUserId())
                .title(task.getTitle())
                .nickName(task.getNickName())
                .status(task.getStatus())
                .taskText(task.getTaskText())
                .createdAt(task.getCreatedAt())
                .build();
    }







}
