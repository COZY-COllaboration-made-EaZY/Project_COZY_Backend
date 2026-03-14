package com.ohgiraffers.COZYbe.domain.help.service;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.help.dto.CreateHelpDTO;
import com.ohgiraffers.COZYbe.domain.help.dto.UpdateHelpDTO;
import com.ohgiraffers.COZYbe.domain.help.entity.Help;
import com.ohgiraffers.COZYbe.domain.help.repository.HelpRepository;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.UserRole;
import com.ohgiraffers.COZYbe.domain.user.domain.service.UserDomainService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HelpService {

    private static final String STATUS_WAIT = "WAIT";
    private static final String STATUS_DONE = "DONE";

    private final HelpRepository inquiryRepository;
    private final UserDomainService userDomainService;

    public List<Help> findAll() {
        return inquiryRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Transactional
    public Help createHelp(CreateHelpDTO dto, Jwt jwt) {
        String writer = jwt.getSubject();
        Help inquiry = Help.builder()
                .type(dto.type())
                .title(dto.title())
                .content(dto.content())
                .status(STATUS_WAIT)
                .writer(writer)
                .build();
        log.info("Create Help");
        return inquiryRepository.save(inquiry);
    }

    @Transactional
    public Help updateHelp(Long id, UpdateHelpDTO dto, Jwt jwt){
        if (!isOperator(jwt)) {
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
        }
        Help help = inquiryRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_HELP));
        if (dto.title() != null) {
            help.setTitle(dto.title());
        }
        if (dto.content() != null) {
            help.setContent(dto.content());
        }

        if (!isOperator(jwt)) {
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
        }

        if (dto.answer() != null) {
            help.setAnswer(dto.answer());
            help.setAnsweredAt(LocalDateTime.now());
            help.setStatus(dto.status() != null ? dto.status() : STATUS_DONE);
        } else if (dto.status() != null) {
            help.setStatus(dto.status());
        }

        log.info("Update Help");
        return help;
    }

    private boolean isOperator(Jwt jwt) {
        if (jwt == null) return false;
        String userId = jwt.getSubject();
        User user = userDomainService.getUser(userId);
        return user.getRole() == UserRole.OPERATOR;
    }


    @Transactional
    public void deleteHelp(Long id, Jwt jwt) {
        if (!isOperator(jwt)) {
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
        }
        Help help = inquiryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inquiry not found"));
        inquiryRepository.delete(help);
    }


}

