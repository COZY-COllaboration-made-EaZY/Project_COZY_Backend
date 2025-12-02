package com.ohgiraffers.COZYbe.domain.user.application.controller;


import com.ohgiraffers.COZYbe.domain.user.application.dto.SignUpDTO;
import com.ohgiraffers.COZYbe.domain.user.application.dto.UserInfoDTO;
import com.ohgiraffers.COZYbe.domain.user.application.dto.UserUpdateDTO;
import com.ohgiraffers.COZYbe.domain.user.application.service.UserAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserAppService userAppService;

    @Operation(summary = "구 회원가입", description = "프로필 이미지는 따로 처리 하는게 나음", deprecated = true)
    @PostMapping(value = "/signup", consumes = { "multipart/form-data" })
    public ResponseEntity<?> signup(
            @RequestPart("signUpDTO") String signUpDTOJson,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {

//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            SignUpDTO signUpDTO = objectMapper.readValue(signUpDTOJson, SignUpDTO.class);
//
//            if (!Objects.equals(signUpDTO.getConfirmPassword(), signUpDTO.getPassword())) {
//                return ResponseEntity.badRequest().body(Map.of("error", "비밀번호가 일치하지 않습니다."));
//            }
//
//            User user = userAppService.register(signUpDTO, profileImage);
//            return ResponseEntity.ok(user);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(Map.of("error", "회원가입 중 오류 발생: " + e.getMessage()));
//        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Operation(summary = "회원가입", description = "프로필 이미지는 디폴트 이미지로 자동 적용")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
            @ApiResponse(responseCode = "500", description = "예상치 못한 예러")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignUpDTO signUpDTO){
        UserInfoDTO userInfoDTO = userAppService.registerDefault(signUpDTO);
        return ResponseEntity.ok(userInfoDTO);
    }


    @Operation(summary = "구 현재 회원", description = "잘못된 토큰 처리", deprecated = true)
    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
//        log.info(token);
//        if (token == null || !token.startsWith("Bearer ")) {
//            return ResponseEntity.status(401).body(Map.of("error", "인증 토큰이 필요합니다."));
//        }
//
//        try {
//            String jwt = token.substring(7);
//            String userId = authService.getUserIdFromToken(jwt);
//            UserInfoDTO userInfoDTO = userAppService.getUserInfo(userId);
//            return ResponseEntity.ok(userInfoDTO);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body(Map.of("error", "사용자 조회 실패: " + e.getMessage()));
//        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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

    @Operation(summary = "패스워드 재확인", description = "잘못된 처리", deprecated = true)
    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> request) {
//        if (token == null || !token.startsWith("Bearer ")) {
//            return ResponseEntity.status(401).body(Map.of("error", "인증 토큰이 없습니다."));
//        }
//
//        System.out.println("받은 인증 토큰: " + token);
//
//
//        String userId;
//        try {
//            userId = authService.getUserIdFromToken(token.substring(7)); // "Bearer " 제거 후 이메일 추출
//        } catch (Exception e) {
//            return ResponseEntity.status(400).body(Map.of("error", "유효하지 않은 토큰입니다."));
//        }
//
//        String inputPassword = request.get("password");
//
//        if (inputPassword == null) {
//            System.out.println("비밀번호가 전달되지 않음");
//            return ResponseEntity.status(400).body(Map.of("error", "비밀번호가 필요합니다."));
//        }
//
//        try {
//            boolean isValid = userAppService.verifyPassword(userId, inputPassword);
//
//            if (isValid) {
//                System.out.println("✅ 비밀번호 확인 성공");
//                return ResponseEntity.ok(Map.of("valid", true));
//            } else {
//                System.out.println("❌ 비밀번호 불일치");
//                return ResponseEntity.status(400).body(Map.of("error", "비밀번호가 일치하지 않습니다."));
//            }
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
//        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    @Operation(summary = "회원정보 수정", description = "프로필 이미지는 따로 처리 하는게 나음", deprecated = true)
    @PostMapping(value = "/update-info", consumes = { "multipart/form-data" })
    public ResponseEntity<?> updateUserInfo(
            @RequestHeader("Authorization") String token,
            @RequestParam("nickname") String nickname,
            @RequestParam("statusMessage") String statusMessage,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {
//        System.out.println("nickname: " + nickname);
//        System.out.println("statusMessage: " + statusMessage);
//        System.out.println("profileImage: " + (profileImage != null ? profileImage.getOriginalFilename() : "없음"));
//
//
//        if (token == null || !token.startsWith("Bearer ")) {
//            return ResponseEntity.status(401).body(Map.of("error", "인증 토큰이 없습니다."));
//        }
//
//        try {
//            String userId = authService.getUserIdFromToken(token.substring(7));
//
//            UserUpdateDTO dto = new UserUpdateDTO(nickname, statusMessage);
//            User updatedUser = userAppService.updateUserInfo(userId, dto, profileImage);
//
//            return ResponseEntity.ok(updatedUser);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body(Map.of("error", "정보 수정 실패: " + e.getMessage()));
//        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    @Operation(summary = "회원정보 수정", description = "null값은 무시됨")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리 되었습니다."),
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
            @ApiResponse(responseCode = "500", description = "예상치 못한 예러")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal Jwt jwt) {
        userAppService.deleteUser(jwt.getSubject());
        return ResponseEntity.ok(Map.of("message", "회원탈퇴가 완료되었습니다."));
    }



}
