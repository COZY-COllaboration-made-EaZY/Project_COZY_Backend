package com.ohgiraffers.COZYbe.domain.inquiry.service;

import com.ohgiraffers.COZYbe.domain.inquiry.dto.InquiryUpdateDTO;
import com.ohgiraffers.COZYbe.domain.inquiry.entity.Inquiry;
import com.ohgiraffers.COZYbe.domain.inquiry.repository.InquiryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    public List<Inquiry> findAll() {
        return inquiryRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Inquiry createInquiry(String type, String title, String content, String writer) {
        Inquiry inquiry = Inquiry.builder()
                .type(type)
                .title(title)
                .content(content)
                .status("처리대기")
                .writer(writer)
                .build();
        return inquiryRepository.save(inquiry);
    }

    @Transactional
    public Inquiry updateInquiry(Long id, InquiryUpdateDTO dto){
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inquiry not found"));

        inquiry.setTitle(dto.getTitle());
        inquiry.setContent(dto.getContent());
        inquiry.setStatus(dto.getStatus());
        return inquiry;
    }

    @Transactional
    public void deleteInquiry(Long id, String writer) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inquiry not found"));
        if (!inquiry.getWriter().equals(writer)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permission to delete this inquiry");
        }
        inquiryRepository.delete(inquiry);
    }


}


