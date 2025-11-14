package com.ohgiraffers.COZYbe.domain.user.application.dto;

import lombok.Data;

@Data
public class SignUpDTO {
    private String email;
    private String password;
    private String confirmPassword;
    private String nickname;
    private String receiveEmail;
    private String captcha;

    // ✅ 상태 메시지 필드 추가
    private String statusMessage;

}
