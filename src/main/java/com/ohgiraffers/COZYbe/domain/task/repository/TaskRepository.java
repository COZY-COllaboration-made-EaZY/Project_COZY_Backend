//package com.ohgiraffers.COZYbe.domain.task.repository;
//
//import com.ohgiraffers.COZYbe.domain.task.entity.Task;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.UUID;
//
//// select from Task
//public interface TaskRepository extends JpaRepository<Task, Long> {
//
//    List<Task> findAllByProject_ProjectId(Long projectId);
//
//
//}
