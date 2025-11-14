//package com.ohgiraffers.COZYbe.domain.task.dto;
//
//import com.ohgiraffers.COZYbe.domain.task.entity.Task;
//import lombok.Builder;
//
//import java.time.LocalDateTime;
//
//@Builder
//public record TaskResponseDTO(
//        Long id,
//        String title,
//        String status,
//        String taskText,
//        String nickName,
//        LocalDateTime createdAt
//) {
//    public static TaskResponseDTO from(Task t) {
//        return TaskResponseDTO.builder()
//                .id(t.getTaskId())
//                .title(t.getTitle())
//                .status(t.getStatus())
//                .taskText(t.getTaskText())
//                .nickName(t.getNickName())
//                .createdAt(t.getCreatedAt())
//                .build();
//    }
//}
//
