package com.ohgiraffers.COZYbe.domain.personal.controller;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.personal.dto.*;
import com.ohgiraffers.COZYbe.domain.personal.service.PersonalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "개인", description = "개인 메모/일정/게시판 API")
@RestController
@RequestMapping("/api/personal")
@RequiredArgsConstructor
public class PersonalController {

    private final PersonalService personalService;

    private String requireUser(Jwt jwt) {
        if (jwt == null) throw new ApplicationException(ErrorCode.ANONYMOUS_USER);
        return jwt.getSubject();
    }

    // ===== Memo =====
    @Operation(summary = "개인 메모 리스트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "401", description = "로그인 필요")
    })
    @GetMapping("/memos")
    public ResponseEntity<List<PersonalMemoResponse>> getMemos(@AuthenticationPrincipal Jwt jwt) {
        String userId = requireUser(jwt);
        return ResponseEntity.ok(personalService.getMemos(userId));
    }

    @Operation(summary = "개인 메모 생성")
    @PostMapping("/memos")
    public ResponseEntity<PersonalMemoResponse> createMemo(@AuthenticationPrincipal Jwt jwt,
                                                           @RequestBody PersonalMemoCreateDTO dto) {
        String userId = requireUser(jwt);
        return ResponseEntity.ok(personalService.createMemo(dto, userId));
    }

    @Operation(summary = "개인 메모 수정")
    @PatchMapping("/memos/{memoId}")
    public ResponseEntity<PersonalMemoResponse> updateMemo(@AuthenticationPrincipal Jwt jwt,
                                                           @PathVariable UUID memoId,
                                                           @RequestBody PersonalMemoUpdateDTO dto) {
        String userId = requireUser(jwt);
        return ResponseEntity.ok(personalService.updateMemo(memoId, dto, userId));
    }

    @Operation(summary = "개인 메모 삭제")
    @DeleteMapping("/memos/{memoId}")
    public ResponseEntity<?> deleteMemo(@AuthenticationPrincipal Jwt jwt,
                                        @PathVariable UUID memoId) {
        String userId = requireUser(jwt);
        personalService.deleteMemo(memoId, userId);
        return ResponseEntity.noContent().build();
    }

    // ===== Schedule =====
    @Operation(summary = "개인 일정 리스트")
    @GetMapping("/schedules")
    public ResponseEntity<List<PersonalScheduleResponse>> getSchedules(@AuthenticationPrincipal Jwt jwt) {
        String userId = requireUser(jwt);
        return ResponseEntity.ok(personalService.getSchedules(userId));
    }

    @Operation(summary = "개인 일정 생성")
    @PostMapping("/schedules")
    public ResponseEntity<PersonalScheduleResponse> createSchedule(@AuthenticationPrincipal Jwt jwt,
                                                                   @RequestBody PersonalScheduleCreateDTO dto) {
        String userId = requireUser(jwt);
        return ResponseEntity.ok(personalService.createSchedule(dto, userId));
    }

    @Operation(summary = "개인 일정 수정")
    @PatchMapping("/schedules/{scheduleId}")
    public ResponseEntity<PersonalScheduleResponse> updateSchedule(@AuthenticationPrincipal Jwt jwt,
                                                                   @PathVariable UUID scheduleId,
                                                                   @RequestBody PersonalScheduleUpdateDTO dto) {
        String userId = requireUser(jwt);
        return ResponseEntity.ok(personalService.updateSchedule(scheduleId, dto, userId));
    }

    @Operation(summary = "개인 일정 삭제")
    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@AuthenticationPrincipal Jwt jwt,
                                            @PathVariable UUID scheduleId) {
        String userId = requireUser(jwt);
        personalService.deleteSchedule(scheduleId, userId);
        return ResponseEntity.noContent().build();
    }

    // ===== Personal Post =====
    @Operation(summary = "개인 게시글 리스트")
    @GetMapping("/posts")
    public ResponseEntity<List<PersonalPostResponse>> getPosts(@AuthenticationPrincipal Jwt jwt) {
        String userId = requireUser(jwt);
        return ResponseEntity.ok(personalService.getPosts(userId));
    }

    @Operation(summary = "개인 게시글 생성")
    @PostMapping("/posts")
    public ResponseEntity<PersonalPostResponse> createPost(@AuthenticationPrincipal Jwt jwt,
                                                           @RequestBody PersonalPostCreateDTO dto) {
        String userId = requireUser(jwt);
        return ResponseEntity.ok(personalService.createPost(dto, userId));
    }

    @Operation(summary = "개인 게시글 수정")
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<PersonalPostResponse> updatePost(@AuthenticationPrincipal Jwt jwt,
                                                           @PathVariable UUID postId,
                                                           @RequestBody PersonalPostUpdateDTO dto) {
        String userId = requireUser(jwt);
        return ResponseEntity.ok(personalService.updatePost(postId, dto, userId));
    }

    @Operation(summary = "개인 게시글 삭제")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal Jwt jwt,
                                        @PathVariable UUID postId) {
        String userId = requireUser(jwt);
        personalService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }
}
