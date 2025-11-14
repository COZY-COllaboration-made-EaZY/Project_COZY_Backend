package com.ohgiraffers.COZYbe.domain.inquiry.controller;

import com.ohgiraffers.COZYbe.domain.inquiry.dto.InquiryDTO;
import com.ohgiraffers.COZYbe.domain.inquiry.dto.InquiryUpdateDTO;
import com.ohgiraffers.COZYbe.domain.inquiry.entity.Inquiry;
import com.ohgiraffers.COZYbe.domain.inquiry.service.InquiryService;
import com.ohgiraffers.COZYbe.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
// inquiry.tsx
public class InquiryController {

    private static final Logger log = LoggerFactory.getLogger(InquiryController.class);
    private final InquiryService inquiryService;
    private final JwtTokenProvider jwtTokenProvider;

    // Get information from all inquiries.
    @GetMapping("/list")
    public List<Inquiry> getAll() {
        return inquiryService.findAll();
    }

    // Create Inquiry
    @PostMapping("/create")
    public Inquiry create(@RequestBody InquiryDTO request, HttpServletRequest servletRequest) {
        String token = servletRequest.getHeader("Authorization").substring(7);
        String writer = jwtTokenProvider.decodeUserIdFromJwt(token);
        return inquiryService.createInquiry(
                request.getType(),
                request.getTitle(),
                request.getContent(),
                writer
        );
    }

    // Update Inquiry
    @PutMapping("/{id}")
    public Inquiry updateInquiry(@PathVariable Long id,
                                 @RequestBody @Valid InquiryUpdateDTO dto,
                                 HttpServletRequest servletRequest) {
        String token = servletRequest.getHeader("Authorization").substring(7);
        String userId = jwtTokenProvider.decodeUserIdFromJwt(token);
        log.info("Update Inquiry OK");
        return inquiryService.updateInquiry(id, dto);
    }


    // Deleted Inquiry
    @DeleteMapping("/{id}")
    public void deleteInquiry(@PathVariable Long id, HttpServletRequest servletRequest) {
        String token = servletRequest.getHeader("Authorization").substring(7);
        String userId = jwtTokenProvider.decodeUserIdFromJwt(token);
        log.info("Deleted Inquiry OK");
        inquiryService.deleteInquiry(id, userId);
    }
}

