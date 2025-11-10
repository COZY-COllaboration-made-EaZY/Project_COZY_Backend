package com.ohgiraffers.COZYbe.domain.joinrequest.application.controller;


import com.ohgiraffers.COZYbe.domain.joinrequest.application.dto.request.CreateJoinRequestDTO;
import com.ohgiraffers.COZYbe.domain.joinrequest.application.dto.response.JoinRequestDTO;
import com.ohgiraffers.COZYbe.domain.joinrequest.application.dto.response.JoinRequestListDTO;
import com.ohgiraffers.COZYbe.domain.joinrequest.application.service.JoinRequestAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@Tag(name = "팀 가입 요청", description = "가입요청 관련 api")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/join-request")
public class JoinRequestController {

    private final JoinRequestAppService joinRequestAppService;

    @Operation(summary = "팀 가입 요청 생성", description = "특정 팀에 가입 요청을 보냅니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "409", description = "이미 요청했거나 가입한 팀입니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 팀입니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 에러")
    })
    @PostMapping
    public ResponseEntity<?> createJoinRequest(
            @RequestBody CreateJoinRequestDTO createDTO,
            @AuthenticationPrincipal Jwt jwt) {
        JoinRequestDTO result = joinRequestAppService.createJoinRequest(createDTO, jwt.getSubject());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "내가 보낸 가입 요청 목록 조회", description = "내가 보낸 모든 가입 요청 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 에러")
    })
    @GetMapping("/my-requests")
    public ResponseEntity<JoinRequestListDTO> getMyRequests(@AuthenticationPrincipal Jwt jwt) {
        JoinRequestListDTO result = joinRequestAppService.getMyRequests(jwt.getSubject());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "팀에 온 가입 요청 목록 조회", description = "팀에 온 대기 중인 가입 요청 목록을 조회합니다. (리더/서브리더만 가능)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 팀입니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 에러")
    })
    @GetMapping("/team-requests")
    public ResponseEntity<JoinRequestListDTO> getTeamRequests(
            @RequestParam String teamId,
            @AuthenticationPrincipal Jwt jwt) {
        JoinRequestListDTO result = joinRequestAppService.getTeamRequests(teamId, jwt.getSubject());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "가입 요청 승인", description = "가입 요청을 승인하고 멤버로 등록합니다. (리더/서브리더만 가능)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 요청입니다."),
            @ApiResponse(responseCode = "409", description = "이미 처리된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 에러")
    })
    @PatchMapping("/{requestId}/approve")
    public ResponseEntity<?> approveRequest(
            @PathVariable String requestId,
            @AuthenticationPrincipal Jwt jwt) {
        joinRequestAppService.approveRequest(requestId, jwt.getSubject());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "가입 요청 거부", description = "가입 요청을 거부합니다. (리더/서브리더만 가능)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 요청입니다."),
            @ApiResponse(responseCode = "409", description = "이미 처리된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 에러")
    })
    @PatchMapping("/{requestId}/reject")
    public ResponseEntity<?> rejectRequest(
            @PathVariable String requestId,
            @AuthenticationPrincipal Jwt jwt) {
        joinRequestAppService.rejectRequest(requestId, jwt.getSubject());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "가입 요청 취소", description = "내가 보낸 가입 요청을 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공적으로 삭제되었습니다."),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 요청입니다."),
            @ApiResponse(responseCode = "409", description = "이미 처리된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 에러")
    })
    @DeleteMapping("/{requestId}")
    public ResponseEntity<?> cancelRequest(
            @PathVariable String requestId,
            @AuthenticationPrincipal Jwt jwt) {
        joinRequestAppService.cancelRequest(requestId, jwt.getSubject());
        return ResponseEntity.noContent().build();
    }
}
