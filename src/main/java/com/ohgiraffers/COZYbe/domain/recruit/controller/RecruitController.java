package com.ohgiraffers.COZYbe.domain.recruit.controller;

import com.ohgiraffers.COZYbe.domain.recruit.dto.RecruitCreateDTO;
import com.ohgiraffers.COZYbe.domain.recruit.dto.RecruitListResponse;
import com.ohgiraffers.COZYbe.domain.recruit.dto.RecruitUpdateDTO;
import com.ohgiraffers.COZYbe.domain.recruit.entity.Recruit;
import com.ohgiraffers.COZYbe.domain.recruit.service.RecruitService;
import com.ohgiraffers.COZYbe.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/recruit")
@RequiredArgsConstructor
public class RecruitController {

    private static final Logger log = LoggerFactory.getLogger(RecruitController.class);
    private final RecruitService recruitService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/list")
    public ResponseEntity<List<RecruitListResponse>> getAll() {
        return ResponseEntity.ok(recruitService.findAll());
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createRecruit(
            @RequestBody RecruitCreateDTO dto,
            @AuthenticationPrincipal Jwt user
    ) {
        recruitService.createRecruit(dto, user.getId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public Recruit updateRecruit(@PathVariable Long id,
                                 @RequestBody @Valid RecruitUpdateDTO dto,
                                 HttpServletRequest servletRequest){
        String token = servletRequest.getHeader("Authorization").substring(7);
        String userId = jwtTokenProvider.decodeUserIdFromJwt(token);
        log.info("Update Recruit");
        return recruitService.updateRecruit(id,dto,userId);
    }

    @DeleteMapping("/{id}")
    public void deletedRecruit(@PathVariable Long id, HttpServletRequest req) {
        String auth = req.getHeader("Authorization");
        String userId = jwtTokenProvider.decodeUserIdFromJwt(auth.substring(7));
        log.info("Deleted Recruit");
        recruitService.deleteRecruit(id, userId);
    }


}
