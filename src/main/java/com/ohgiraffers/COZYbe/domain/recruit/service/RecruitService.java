package com.ohgiraffers.COZYbe.domain.recruit.service;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.recruit.dto.RecruitCreateDTO;
import com.ohgiraffers.COZYbe.domain.recruit.dto.RecruitDetailResponse;
import com.ohgiraffers.COZYbe.domain.recruit.dto.RecruitListResponse;
import com.ohgiraffers.COZYbe.domain.recruit.dto.RecruitUpdateDTO;
import com.ohgiraffers.COZYbe.domain.recruit.entity.Recruit;
import com.ohgiraffers.COZYbe.domain.recruit.repository.RecruitRepository;
import com.ohgiraffers.COZYbe.domain.teams.domain.entity.Team;
import com.ohgiraffers.COZYbe.domain.teams.domain.repository.TeamRepository;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
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
    private final TeamRepository teamRepository;

    @Transactional
    public List<RecruitListResponse> findAll() {
        return recruitRepository.findAllWithTeam().stream()
                .map(recruit -> new RecruitListResponse(
                        recruit.getRecruitId(),
                        recruit.getTitle(),
                        recruit.getNickName(),
                        recruit.getRecruitText(),
                        recruit.getTeam().getTeamName(),
                        recruit.getTeam().getTeamId().toString(),
                        recruit.getCreatedAt()
                ))
                .toList();
    }

    @Transactional
    public RecruitDetailResponse getDetail(Long id) {

        Recruit recruit = recruitRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_RECRUIT));
        System.out.println("팀이름 :: " + recruit.getTeam().getTeamName());

        Team team = recruit.getTeam();
        System.out.println("teamName :: " + team.getTeamName());
        System.out.println("teamId :: " + team.getTeamId());
        return new RecruitDetailResponse(
                recruit.getRecruitId(),
                recruit.getTitle(),
                recruit.getRecruitText(),
                recruit.getNickName(),
                team.getTeamId().toString(),
                team.getTeamName(),
                recruit.getCreatedAt()
        );
    }

    @Transactional
    public void createRecruit(RecruitCreateDTO dto, String writer) {
        Team team = teamRepository.findById(dto.teamId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_TEAM));
        System.out.println("teamName :: " + team.getTeamName());
        System.out.println("teamId :: " + team.getTeamId());

        Recruit recruit = Recruit.builder()
                .title(dto.title())
                .nickName(dto.nickName())
                .recruitText(dto.recruitText())
                .writer(writer)
                .team(team)
                .build();

        recruitRepository.save(recruit);
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