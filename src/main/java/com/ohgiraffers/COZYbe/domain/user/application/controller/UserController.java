package com.ohgiraffers.COZYbe.domain.user.application.controller;


import com.ohgiraffers.COZYbe.domain.auth.application.dto.LoginDTO;
import com.ohgiraffers.COZYbe.domain.user.application.dto.SignUpDTO;
import com.ohgiraffers.COZYbe.domain.user.application.dto.UserInfoDTO;
import com.ohgiraffers.COZYbe.domain.user.application.dto.UserUpdateDTO;
import com.ohgiraffers.COZYbe.domain.user.application.dto.ValidDTO;
import com.ohgiraffers.COZYbe.domain.user.application.service.UserAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserAppService userAppService;

    @Operation(summary = "회원가입", description = "프로필 이미지는 디폴트 이미지로 자동 적용")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "422", description = "이메일이 중복이거나 유효하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 예러")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignUpDTO signUpDTO){
        UserInfoDTO userInfoDTO = userAppService.registerDefault(signUpDTO);
        return ResponseEntity.ok(userInfoDTO);
    }

    @Operation(summary = "이메일 중복 확인", description = "boolean 값으로 리턴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 예러")
    })
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmailDuplicate(@RequestParam String email) {
        boolean isAvailable = userAppService.isEmailAvailable(email);
        return ResponseEntity.ok(Map.of("available", isAvailable));
    }

    @Operation(summary = "패스워드 재확인", description = "로그인된 유저의 패스워드 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다."),
            @ApiResponse(responseCode = "404", description = "해당 유저는 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 예러")
    })
    public ResponseEntity<?> verifyPassword(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody LoginDTO loginDTO) {

        Boolean isValid = userAppService.verifyPassword(jwt.getSubject(), loginDTO.getPassword());
        return ResponseEntity.ok().body(new ValidDTO(isValid));

    }

    @Operation(summary = "회원정보 수정", description = "null값은 무시됨")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다."),
            @ApiResponse(responseCode = "404", description = "해당 유저는 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 예러")
    })
    @PatchMapping("/update")
    public ResponseEntity<?> updateUser(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody UserUpdateDTO updateDTO
    ){
        String userId = jwt.getSubject();
        UserInfoDTO updated = userAppService.updateUser(userId, updateDTO);
        return ResponseEntity.ok().body(updated);
    }


    @Operation(summary = "현재 유저 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다."),
            @ApiResponse(responseCode = "404", description = "해당 유저는 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 예러")
    })
    @GetMapping("/check-current")
    public ResponseEntity<?> checkCurrentUser(@AuthenticationPrincipal Jwt jwt){
        String sub = jwt.getSubject();
        UserInfoDTO userInfoDTO = userAppService.getUserInfo(sub);
        return ResponseEntity.ok(userInfoDTO);
    }

    @Operation(summary = "회원탈퇴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "401", description = "로그인이 필요합니다."),
            @ApiResponse(responseCode = "404", description = "해당 유저는 존재하지 않습니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 예러")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal Jwt jwt) {
        userAppService.deleteUser(jwt.getSubject());
        return ResponseEntity.ok(Map.of("message", "회원탈퇴가 완료되었습니다."));
    }



}
