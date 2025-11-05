package com.ohgiraffers.COZYbe.domain.member.application.controller;

import com.ohgiraffers.COZYbe.domain.member.application.dto.response.MemberListDTO;
import com.ohgiraffers.COZYbe.domain.member.application.service.MemberAppService;
import com.ohgiraffers.COZYbe.domain.teams.application.dto.request.TeamIdDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
@Tag(name = "멤버 컨트롤러", description = "멤버와 관련된 api")
public class MemberController {

    private final MemberAppService service;



    @Operation(summary = "팀원 조회", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 예러")
    })
    @GetMapping("/list")
    public ResponseEntity<?> getMemberlist(TeamIdDTO teamIdDTO,
                           @AuthenticationPrincipal Jwt jwt){
        MemberListDTO dto = service.getMemberList(teamIdDTO.teamId());
        return ResponseEntity.ok(dto);
    }


    @Operation(summary = "팀에 가입하기", description = "팀id를 가지고 허가받지 않고 가입. 승인요청 만들고 나서 deprecated 예정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 예러")
    })
    @PostMapping
    public ResponseEntity<?> joinMember(TeamIdDTO teamIdDTO,
                                        @AuthenticationPrincipal Jwt jwt){
        service.joinMember(teamIdDTO.teamId(),jwt.getSubject());
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "팀에 탈퇴하기", description = "팀에서 탈퇴하기, 허가 필요없음")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 예러")
    })
    @DeleteMapping
    public ResponseEntity<?> leaveMember(TeamIdDTO teamIdDTO,
                            @AuthenticationPrincipal Jwt jwt){
        service.leaveMember(teamIdDTO.teamId(),jwt.getSubject());
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "유저가 가입한 팀 조회", description = "타인의 팀 가입 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 예러")
    })
    @GetMapping("/find-join")
    public void isMemberJoined(){

    }


}
