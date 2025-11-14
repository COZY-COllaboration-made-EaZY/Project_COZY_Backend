package com.ohgiraffers.COZYbe.domain.inquiry.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InquiryUpdateDTO {
    private String title;
    private String content;
    private String status;
}
