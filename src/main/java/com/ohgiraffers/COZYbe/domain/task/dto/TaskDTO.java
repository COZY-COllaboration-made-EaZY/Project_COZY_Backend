package com.ohgiraffers.COZYbe.domain.task.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// タスクを作る時に必要な情報。
@Getter
@Setter
@ToString
public class TaskDTO {

    private Long projectId;

    // ニックネーム
    @NotBlank
    private String nickName;

    // タイトル
    @NotBlank
    private String title;

    // 状況
    @NotBlank
    private String status;

    // 内容
    @NotBlank
    private String taskText;
}
