package com.ohgiraffers.COZYbe.domain.recruit.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Data
public class RecruitCreateDTO {
    private String title;
    private String nickName;
    private String recruitText;
}
