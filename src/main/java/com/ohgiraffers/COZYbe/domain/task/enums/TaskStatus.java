package com.ohgiraffers.COZYbe.domain.task.enums;

public enum TaskStatus {
    BEFORE,     // 시작 전
    DOING,      // 진행 중
    REVIEW,     // 검토 중
    APPROVING,  // 승인 중
    MERGE_REQ,  // 머지 신청
    MERGED      // 머지 완료
}
