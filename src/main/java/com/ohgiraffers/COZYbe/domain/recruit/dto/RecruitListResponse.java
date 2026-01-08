package com.ohgiraffers.COZYbe.domain.recruit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RecruitListResponse {

    private Long id;
    private String title;
    private String nickName;
    private String recruitText;
    private String teamName;
    private String teamId;
    private LocalDateTime createdAt;
}

