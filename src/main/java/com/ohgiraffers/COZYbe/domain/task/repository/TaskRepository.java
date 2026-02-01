package com.ohgiraffers.COZYbe.domain.task.repository;

import com.ohgiraffers.COZYbe.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByProject_ProjectId(UUID projectId);
//    Optional<Task> findByTaskId(UUID taskId);
}
