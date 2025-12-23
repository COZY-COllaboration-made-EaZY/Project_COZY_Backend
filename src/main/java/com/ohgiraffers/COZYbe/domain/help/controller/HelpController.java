package com.ohgiraffers.COZYbe.domain.help.controller;

import com.ohgiraffers.COZYbe.domain.help.dto.CreateHelpDTO;
import com.ohgiraffers.COZYbe.domain.help.dto.UpdateHelpDTO;
import com.ohgiraffers.COZYbe.domain.help.entity.Help;
import com.ohgiraffers.COZYbe.domain.help.service.HelpService;
import com.ohgiraffers.COZYbe.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/help")
@RequiredArgsConstructor
public class HelpController {

    private final HelpService helpService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/list")
    public List<Help> getAll() {
        return helpService.findAll();
    }

    // Create Inquiry
    @PostMapping("/create")
    public Help create(@RequestBody CreateHelpDTO createDTO, @AuthenticationPrincipal Jwt jwt) {
        return helpService.createHelp(createDTO,jwt);
    }

    // Update Inquiry
    @PutMapping("/{id}")
    public Help updateInquiry(@PathVariable Long id, @RequestBody UpdateHelpDTO dto) {
        return helpService.updateHelp(id, dto);
    }


    // Deleted Inquiry
    @DeleteMapping("/{id}")
    public void deleteInquiry(@PathVariable Long id) {
        helpService.deleteHelp(id);
    }

}

