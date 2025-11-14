package com.ohgiraffers.COZYbe.domain.teams.domain.service;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.teams.domain.entity.Team;
import com.ohgiraffers.COZYbe.domain.teams.domain.repository.TeamRepository;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class TeamDomainService {

    private TeamRepository repository;

    public Team createTeam(String teamName, String description, User leader) {
        return repository.save(Team.builder()
                .teamName(teamName)
                .description(description)
                .leader(leader)
                .build());
    }

    public Team getTeam(String teamId) {
        return getTeam(UUID.fromString(teamId));
    }

    public Team getTeam(UUID teamId) {
        return repository.findById(teamId)
                .orElseThrow(this::noTeam);
    }

    public List<Team> findByLeader(String userId) {
        return findByLeader(UUID.fromString(userId));
    }

    public List<Team> findByLeader(UUID userId) {
        return repository.findByLeaderUserId(userId)
                .orElseThrow(this::noTeam);
    }

    public List<Team> findByLeader(User leader) {
        return repository.findByLeaderUserId(leader.getUserId())
                .orElseThrow(this::noTeam);
    }

    public List<Team> searchByName(String searchKeyword) {
        return repository.findByTeamNameContainingIgnoreCase(searchKeyword);
    }

    public List<Team> getAllTeams() {
        return repository.findAll();
    }

    public List<Team> getAllById(List<UUID> teamIds) {
        return repository.findAllById(teamIds);
    }

    public Team saveTeam(Team team) {
        return repository.save(team);
    }

    public void deleteTeam(Team team) {
        repository.delete(team);
    }

    public void deleteTeam(String teamId) {
        deleteTeam(UUID.fromString(teamId));
    }

    public void deleteTeam(UUID teamId) {
        repository.delete(getTeam(teamId));
    }

    public boolean isTeamExist(String teamId) {
        return isTeamExist(UUID.fromString(teamId));
    }

    public boolean isTeamExist(UUID teamId) {
        return repository.existsById(teamId);
    }

    public boolean isTeamNameExist(String teamName){
        return repository.existsByTeamName(teamName);
    }

    public Team getReference(String teamId) {
        return getReference(UUID.fromString(teamId));
    }

    public Team getReference(UUID teamId) {
        return repository.getReferenceById(teamId);
    }

    private ApplicationException noTeam() {
        return new ApplicationException(ErrorCode.NO_SUCH_TEAM);
    }
}
