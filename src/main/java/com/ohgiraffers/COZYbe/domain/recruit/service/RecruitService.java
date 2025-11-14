package com.ohgiraffers.COZYbe.domain.recruit.service;

import com.ohgiraffers.COZYbe.domain.recruit.dto.RecruitCreateDTO;
import com.ohgiraffers.COZYbe.domain.recruit.dto.RecruitUpdateDTO;
import com.ohgiraffers.COZYbe.domain.recruit.entity.Recruit;
import com.ohgiraffers.COZYbe.domain.recruit.repository.RecruitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitService {

    private final RecruitRepository recruitRepository;

    public List<Recruit> findAll() {
        return recruitRepository.findAll();
    }

    @Transactional
    public Recruit createRecruit(String title, String nickName, String recruitText, String writer){
        Recruit recruit = Recruit.builder()
                .title(title)
                .nickName(nickName)
                .recruitText(recruitText)
                .writer(writer)
                .build();
        return recruitRepository.save(recruit);
    }

    @Transactional
    public Recruit updateRecruit(Long id, RecruitUpdateDTO dto, String writer){
        Recruit recruit = recruitRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found"));
        recruit.setTitle(dto.getTitle());
        recruit.setRecruitText(dto.getRecruitText());
        return recruit;
    }

    @Transactional
    public void deleteRecruit(Long id, String writer){
        Recruit recruit = recruitRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found"));
        recruitRepository.delete(recruit);

    }

    @Transactional
    public Recruit getDetailRecruit(Long id) {
        return recruitRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found"));
    }
}