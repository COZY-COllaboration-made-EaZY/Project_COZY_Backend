package com.ohgiraffers.COZYbe.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR-000", "Internal server error"),
    SAME_EMAIL(HttpStatus.CONFLICT, "USER-001", "이미 가입된 이메일입니다."),
    NO_SUCH_USER(HttpStatus.NOT_FOUND, "USER-002", "해당 유저는 존재하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.UNPROCESSABLE_ENTITY, "USER-003", "비밀번호가 유효하지 않습니다."),
    INVALID_EMAIL(HttpStatus.UNPROCESSABLE_ENTITY, "USER-004", "이메일이 중복이거나 유효하지 않습니다."),
    FAILED_GET_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN-001", "Access Token 을 가져오는데 실패했습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN-002", "Token 이 유효하지 않습니다."),
    INVALID_USER(HttpStatus.NOT_FOUND, "TOKEN-003", "UserID 가 유효하지 않습니다."),
    NOT_ALLOWED(HttpStatus.FORBIDDEN, "AUTH-001", "허가되지 않은 접근."),
    ANONYMOUS_USER(HttpStatus.UNAUTHORIZED, "AUTH-002", "익명의 유저가 접근하였습니다."),
    NO_SUCH_TEAM(HttpStatus.NOT_FOUND, "TEAM-001", "존재하지 않는 팀."),
    NO_SUCH_MEMBER(HttpStatus.NOT_FOUND, "MEMBER-001", "존재하지 않는 멤버."),
    NO_SUCH_PROJECT(HttpStatus.NOT_FOUND, "PROJECT-001","존재하지 않는 프로젝트")
    ;

    private HttpStatus status;
    private String errorCode;
    private String message;
}
