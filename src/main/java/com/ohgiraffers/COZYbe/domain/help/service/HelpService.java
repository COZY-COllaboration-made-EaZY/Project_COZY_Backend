package com.ohgiraffers.COZYbe.domain.help.service;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.help.dto.CreateHelpDTO;
import com.ohgiraffers.COZYbe.domain.help.dto.UpdateHelpDTO;
import com.ohgiraffers.COZYbe.domain.help.entity.Help;
import com.ohgiraffers.COZYbe.domain.help.repository.HelpRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HelpService {

    private final HelpRepository inquiryRepository;

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
                .status("처리대기")
                .writer(writer)
                .build();
        log.info("Create Help");
        return inquiryRepository.save(inquiry);
    }

    @Transactional
    public Help updateHelp(Long id, UpdateHelpDTO dto){
        Help help = inquiryRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_HELP));
        help.setTitle(dto.title());
        help.setContent(dto.content());
        log.info("Update Help");
        return help;
    }

    @Transactional
    public void deleteHelp(Long id) {
        Help help = inquiryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inquiry not found"));
        inquiryRepository.delete(help);
    }


}


