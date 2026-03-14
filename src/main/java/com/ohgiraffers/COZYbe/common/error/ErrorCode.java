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
    ANONYMOUS_USER(HttpStatus.UNAUTHORIZED, "AUTH-002", "로그인이 필요합니다."),
    INVALID_TEAM_ID(HttpStatus.BAD_REQUEST, "TEAM-000", "팀 ID 가 유효하지 않습니다."),
    INVALID_TEAM_NAME(HttpStatus.BAD_REQUEST, "TEAM-002", "팀 이름이 일치하지 않습니다."),
    NO_SUCH_TEAM(HttpStatus.NOT_FOUND, "TEAM-001", "존재하지 않는 팀."),
    NO_SUCH_RECRUIT(HttpStatus.NOT_FOUND,"RECRUIT-001","존재하지 않는 모집"),
    NO_SUCH_MEMBER(HttpStatus.NOT_FOUND, "MEMBER-001", "존재하지 않는 멤버."),
    NO_SUCH_PROJECT(HttpStatus.NOT_FOUND, "PROJECT-001","존재하지 않는 프로젝트"),
    NO_SUCH_JOIN_REQUEST(HttpStatus.NOT_FOUND, "JOIN-001", "존재하지 않는 가입 요청."),
    NO_SUCH_HELP(HttpStatus.NOT_FOUND,"HELP-001","존재하지 않는 헬프"),
    NO_SUCH_TASK(HttpStatus.NOT_FOUND,"TASK-001","존재하지 않는 테스크"),
    NO_SUCH_POST(HttpStatus.NOT_FOUND, "POST-001", "존재하지 않는 게시글"),
    NO_SUCH_POST_COMMENT(HttpStatus.NOT_FOUND, "POST-002", "존재하지 않는 댓글"),
    NO_SUCH_MEMO(HttpStatus.NOT_FOUND, "PERSONAL-001", "존재하지 않는 메모"),
    NO_SUCH_SCHEDULE(HttpStatus.NOT_FOUND, "PERSONAL-002", "존재하지 않는 일정"),
    NO_SUCH_PERSONAL_POST(HttpStatus.NOT_FOUND, "PERSONAL-003", "존재하지 않는 개인 게시글"),
    INVALID_SCHEDULE_TIME(HttpStatus.UNPROCESSABLE_ENTITY, "PERSONAL-004", "일정 시간이 유효하지 않습니다."),
    NO_SUCH_UPGRADE_REQUEST(HttpStatus.NOT_FOUND, "TEAM-010", "존재하지 않는 승급 요청."),
    NO_SUCH_LEAVE_REQUEST(HttpStatus.NOT_FOUND, "TEAM-011", "존재하지 않는 탈퇴 요청."),
    DUPLICATE_REQUEST(HttpStatus.UNPROCESSABLE_ENTITY, "TEAM-012", "이미 처리 대기 중인 요청이 있습니다."),
    DUPLICATE_JOIN_REQUEST(HttpStatus.CONFLICT, "JOIN-002", "이미 요청한 팀입니다."),
    ALREADY_TEAM_MEMBER(HttpStatus.CONFLICT, "JOIN-003", "이미 가입한 팀입니다."),
    REQUEST_ALREADY_PROCESSED(HttpStatus.CONFLICT, "JOIN-004", "이미 처리된 요청입니다."),
    INVALID_REJECT_REASON(HttpStatus.UNPROCESSABLE_ENTITY, "JOIN-005", "거부 사유가 유효하지 않습니다."),
    ;

    private HttpStatus status;
    private String errorCode;
    private String message;
}
