package com.ohgiraffers.COZYbe.common.error;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 전역 예외 처리 <br>
 * ApplicationException ErrorCode 확인
 * */
@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    /**
     * 예상된, 의도된 Exception 처리
     * enum ErrorCode 에 예외목록 등록되어있음
     * */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> applicationHandler(ApplicationException e){
        log.error("[{}] {} {}",
                e.getErrorCode().getErrorCode() ,
                e.getErrorCode().getStatus().value(),
                e.getErrorCode().getMessage());

        Map<String,Object> data = new HashMap<>();
        data.put("status",e.getErrorCode().getStatus().value());
        data.put("errorCode", e.getErrorCode().getErrorCode());
        data.put("message",e.getErrorCode().getMessage());
        data.put("timestamp", e.getTimestamp());

        return ResponseEntity.status(e.getErrorCode().getStatus()).body(ApiUtils.error(data));
    }

    /**
     * 의도되지 않은, 예상치 못한 Exception 처리
     * */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> applicationHandler(Exception e){
        log.error("Unexpected Error occurs. Message : {}", e.getMessage());
        StackTraceElement[] stack = e.getStackTrace();            //StackTrace 5줄만 출력
        for (int i = 0; i < Math.min(5, stack.length); i++) {
            log.error("at {}", stack[i]);
        }
        ErrorCode error = ErrorCode.INTERNAL_SERVER_ERROR;

        Map<String,Object> data = new HashMap<>();
        data.put("status",error.getStatus().value());
        data.put("errorCode", error.getErrorCode());
        data.put("message",e.getMessage());
        data.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiUtils.error(data));
    }
}