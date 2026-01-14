package com.ohgiraffers.COZYbe.domain.recruit.dto;

import lombok.*;

import java.util.UUID;

public record RecruitCreateDTO(
        String title,
        String nickName,
        String recruitText,
        UUID teamId
) {
}
