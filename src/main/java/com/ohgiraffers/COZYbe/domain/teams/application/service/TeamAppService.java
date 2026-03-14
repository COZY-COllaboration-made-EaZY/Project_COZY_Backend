package com.ohgiraffers.COZYbe.domain.teams.application.service;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.member.domain.entity.Member;
import com.ohgiraffers.COZYbe.domain.member.domain.service.MemberDomainService;
import com.ohgiraffers.COZYbe.domain.member.domain.repository.MemberRepository;
import com.ohgiraffers.COZYbe.domain.projects.repository.ProjectRepository;
import com.ohgiraffers.COZYbe.domain.teams.application.dto.request.CreateTeamDTO;
import com.ohgiraffers.COZYbe.domain.teams.application.dto.request.UpdateSubLeaderDTO;
import com.ohgiraffers.COZYbe.domain.teams.application.dto.request.UpdateTeamDTO;
import com.ohgiraffers.COZYbe.domain.teams.application.dto.response.SearchResultDTO;
import com.ohgiraffers.COZYbe.domain.teams.application.dto.response.TeamNameDTO;
import com.ohgiraffers.COZYbe.domain.teams.application.dto.response.TeamDetailDTO;
import com.ohgiraffers.COZYbe.domain.teams.domain.entity.Team;
import com.ohgiraffers.COZYbe.domain.teams.domain.service.TeamDomainService;
import com.ohgiraffers.COZYbe.domain.user.application.service.UserAppService;
import com.ohgiraffers.COZYbe.domain.user.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class TeamAppService {

    private final TeamDomainService domainService;
    private final TeamMapper mapper;

    private final MemberDomainService memberDomainService;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final UserDomainService userDomainService;
    private final UserAppService userAppService;


    public SearchResultDTO getAllList() {
        List<Team> teams = domainService.getAllTeams();
        List<TeamNameDTO> dtoList = teams.stream()
                .filter(team -> team != null && !Boolean.TRUE.equals(team.getIsDisabled()))
                .map(team -> new TeamNameDTO(
                        String.valueOf(team.getTeamId()),
                        team.getTeamName(),
                        team.getDescription(),
                        null,
                        null
                ))
                .toList();
        return new SearchResultDTO(dtoList);
    }

    @Transactional
    public TeamDetailDTO createTeam(CreateTeamDTO createTeamDTO, String userId) {
        Team newTeam = Team.builder()
                .teamName(createTeamDTO.teamName())
                .description(createTeamDTO.description())
                .leader(userDomainService.getReference(userId))
                .isDisabled(false)
                .build();

        Team created = domainService.saveTeam(newTeam);
        log.info("팀 생성됨 : {}", created.getTeamName());
        memberDomainService.joinMember(created, userDomainService.getUser(userId));
        return mapper.entityToDetail(created);
    }

    public boolean checkTeamNameExist(String teamName) {
        return domainService.isTeamNameExist(teamName);
    }

    public TeamDetailDTO getTeamDetail(String teamId, String userId) {

        if (!memberDomainService.isMemberOfTeam(teamId, userId)) {
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
        }
        Team team = domainService.getTeam(teamId);
        return mapper.entityToDetail(team);
    }

    @Transactional
    public TeamDetailDTO updateTeam(UpdateTeamDTO updateDTO, String userId) {
        Team team = getIfLeader(updateDTO.teamId(), userId);

        if (updateDTO.teamName() != null && !updateDTO.teamName().isEmpty()) {
            team.setTeamName(updateDTO.teamName());
        }

        if (updateDTO.description() != null && !updateDTO.description().isEmpty()) {
            team.setDescription(updateDTO.description());
        }

        return mapper.entityToDetail(team);
    }

    @Transactional
    public void updateSubLeader(UpdateSubLeaderDTO updateDTO, String leaderId) {
        Team team = getIfLeader(updateDTO.teamId(), leaderId);
        Member member = memberDomainService.getMember(updateDTO.teamId(),updateDTO.subLeaderId());
        team.setSubLeader(member.getUser());
    }

    @Transactional
    public void setTeamDeleted(String teamId, String userId, String teamName, String password) {
        Team exist = getIfLeaderOrSub(teamId, userId);
        if (teamName == null || teamName.isBlank() || !teamName.equals(exist.getTeamName())) {
            throw new ApplicationException(ErrorCode.INVALID_TEAM_NAME);
        }
        if (password == null || password.isBlank() || !userAppService.verifyPassword(userId, password)) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }
        exist.disableTeam();
    }


    //Todo later: Elastic Search 로 변경
    public SearchResultDTO searchTeamByKeyword(String searchKeyword, Pageable pageable) {
        List<Team> teamList = domainService.searchByName(searchKeyword);
        teamList.removeIf(Team::getIsDisabled);
        List<TeamNameDTO> dtoList = teamList.stream()
                .filter(team -> team != null && !Boolean.TRUE.equals(team.getIsDisabled()))
                .map(team -> new TeamNameDTO(
                        String.valueOf(team.getTeamId()),
                        team.getTeamName(),
                        team.getDescription(),
                        null,
                        null
                ))
                .toList();
        return new SearchResultDTO(dtoList);
    }

    public SearchResultDTO searchTeamByUser(String userId) {
        List<UUID> teamIds = memberDomainService.findTeamIdsByUser(userId);
        if (teamIds == null || teamIds.isEmpty()) {
            return new SearchResultDTO(List.of());
        }

        List<Team> teams = domainService.getAllById(teamIds);
        if (teams == null || teams.isEmpty()) {
            return new SearchResultDTO(List.of());
        }

        teams.removeIf(team -> team == null || Boolean.TRUE.equals(team.getIsDisabled()));

        List<TeamNameDTO> dtoList = teams.stream()
                .map(team -> {
                    long memberCount = memberRepository.countByTeam_TeamId(team.getTeamId());
                    long projectCount = projectRepository.countByTeam_TeamId(team.getTeamId());
                    return new TeamNameDTO(
                            String.valueOf(team.getTeamId()),
                            team.getTeamName(),
                            team.getDescription(),
                            Math.toIntExact(memberCount),
                            Math.toIntExact(projectCount)
                    );
                })
                .toList();
        return new SearchResultDTO(dtoList);
    }


    ///*********** Verifier ***********///



    /**
     * 팀 리더인지 판단
     *
     * @param teamId 가져올 팀
     * @param userId 팀 리더 일때 return
     * @return Team 엔티티
     *
     */
    private Team getIfLeader(String teamId, String userId) {
        Team team = domainService.getReference(teamId);
        if (team.getIsDisabled()) {
            throw new ApplicationException(ErrorCode.NO_SUCH_TEAM);
        }
        if (team.getLeader().getUserId().toString().equals(userId)) {
            log.info("team ({}) : 리더 권한 승인", teamId);
            return team;
        }
        log.info("team ({}) : 리더 권한 거절", teamId);
        throw new ApplicationException(ErrorCode.NOT_ALLOWED);
    }

    /**
     * 팀 리더/서브리더인지 판단
     */
    private Team getIfLeaderOrSub(String teamId, String userId) {
        Team team = domainService.getReference(teamId);
        if (team.getIsDisabled()) {
            throw new ApplicationException(ErrorCode.NO_SUCH_TEAM);
        }
        boolean isLeader = team.getLeader() != null && team.getLeader().getUserId().toString().equals(userId);
        boolean isSubLeader = team.getSubLeader() != null && team.getSubLeader().getUserId().toString().equals(userId);
        if (isLeader || isSubLeader) {
            log.info("team ({}) : 리더/서브리더 권한 승인", teamId);
            return team;
        }
        log.info("team ({}) : 리더/서브리더 권한 거절", teamId);
        throw new ApplicationException(ErrorCode.NOT_ALLOWED);
    }

}
