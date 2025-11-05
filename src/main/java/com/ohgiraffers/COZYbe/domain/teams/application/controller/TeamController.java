package com.ohgiraffers.COZYbe.domain.teams.application.controller;

import com.ohgiraffers.COZYbe.domain.teams.application.dto.request.CreateTeamDTO;
import com.ohgiraffers.COZYbe.domain.teams.application.dto.request.UpdateSubLeaderDTO;
import com.ohgiraffers.COZYbe.domain.teams.application.dto.request.UpdateTeamDTO;
import com.ohgiraffers.COZYbe.domain.teams.application.dto.response.SearchResultDTO;
import com.ohgiraffers.COZYbe.domain.teams.application.dto.response.TeamDetailDTO;
import com.ohgiraffers.COZYbe.domain.teams.application.service.TeamAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final TeamAppService teamAppService;

    @GetMapping("/list")
    public ResponseEntity<?> getTeamList(){
        return ResponseEntity.ok(teamAppService.getAllList());
    }

    @Operation(summary = "팀 생성", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 예러")
    })
    @PostMapping
    public ResponseEntity<?> createTeam(@RequestBody CreateTeamDTO createTeamDTO,
                                     @AuthenticationPrincipal Jwt jwt){
        TeamDetailDTO detailDTO = teamAppService.createTeam(createTeamDTO, jwt.getSubject());
        return ResponseEntity.ok(detailDTO);
    }

    @Operation(summary = "팀 상세 조회", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 예러")
    })
    @GetMapping
    public ResponseEntity<?> getTeam(@RequestParam(value = "team") String teamId,
                        @AuthenticationPrincipal Jwt jwt){
        log.info("Request team detail by ID : {}", teamId);
        TeamDetailDTO detailDTO = teamAppService.getTeamDetail(teamId, jwt.getSubject());
        return ResponseEntity.ok(detailDTO);
    }

    @Operation(summary = "팀 수정", description = "팀 리더일때 수정 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 예러")
    })
    @PatchMapping
    public ResponseEntity<?> updateTeam(@RequestBody UpdateTeamDTO updateDTO,
                                                    @AuthenticationPrincipal Jwt jwt){
        TeamDetailDTO detailDTO = teamAppService.updateTeam(updateDTO, jwt.getSubject());
        return ResponseEntity.ok(detailDTO);
    }

    @Operation(summary = "팀 삭제", description = "팀 리더일때 삭제 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 예러")
    })
    @DeleteMapping
    public ResponseEntity<?>  deleteTeam(@RequestParam(value = "team") String teamId,
                           @AuthenticationPrincipal Jwt jwt){
        teamAppService.deleteTeam(teamId, jwt.getSubject());
        return ResponseEntity.noContent().build();
    }

    //미완성
//    @GetMapping("/search")
//    public ResponseEntity<?> findTeamByName(@RequestParam(value = "search") String searchKeyword, Pageable pageable){
//        log.info("search keyword : {}", searchKeyword);
//        SearchResultDTO resultDTO = teamService.searchTeamByKeyword(searchKeyword, pageable);
//        return ResponseEntity.ok(resultDTO);
//    }

    @Operation(summary = "내가 가입한 팀 조회", description = "팀 이름을 포함한 리스트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 예러")
    })
    @GetMapping("/my-team")
    public ResponseEntity<?> findMyTeam(@AuthenticationPrincipal Jwt jwt){
        SearchResultDTO resultDTO = teamAppService.searchTeamByUser(jwt.getSubject());
        return ResponseEntity.ok(resultDTO);
    }

    @Operation(summary = "팀 이름 중복 체크", description = "팀이름은 유니크 설정되어있어서 어차피 에러이지만 사전에 체크를 하는게 더 나음")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 예러")
    })
    @GetMapping("/check-team-name")
    public ResponseEntity<?> checkTeamName(@RequestParam String teamName){
        boolean isNameExist = teamAppService.checkTeamNameExist(teamName);
        return ResponseEntity.ok(isNameExist);
    }

    @Operation(summary = "팀 서브리더 수정", description = "서브리더 임명, 변경. 리더만 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 예러")
    })
    @PatchMapping("/sub-leader")
    public ResponseEntity<?> updateSubLeader(@RequestBody UpdateSubLeaderDTO updateDTO,
                                             @AuthenticationPrincipal Jwt jwt) {
        teamAppService.updateSubLeader(updateDTO, jwt.getSubject());
        return ResponseEntity.ok().build();
    }

}
