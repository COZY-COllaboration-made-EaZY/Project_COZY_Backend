package com.ohgiraffers.COZYbe.domain.member.domain.service;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.member.domain.entity.Member;
import com.ohgiraffers.COZYbe.domain.member.domain.repository.MemberRepository;
import com.ohgiraffers.COZYbe.domain.teams.domain.entity.Team;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class MemberDomainService {

    private MemberRepository repository;

    public Member joinMember(Team team, User user) {
        return repository.save(Member.builder().team(team).user(user).build());
    }

    public void deleteMember(String teamId, String userId){
        repository.delete(getMember(teamId,userId));
    }

    public void deleteMember(Member member){
        repository.delete(member);
    }

    public Member getMember(String memberId) {
        return getMember(UUID.fromString(memberId));
    }

    public Member getMember(UUID memberId) {
        return repository.findById(memberId)
                .orElseThrow(this::noMember);
    }

    public Member getMember(String teamId, String userId) {
        return repository.findByTeam_TeamIdAndUser_UserId(
                        UUID.fromString(teamId), UUID.fromString(userId))
                .orElseThrow(this::noMember);
    }

    public List<Member> findByUser(String userId) {
        return findByUser(UUID.fromString(userId));
    }

    public List<Member> findByUser(UUID userId) {
        return repository.findByUser_UserId(userId)
                .orElseThrow(this::noMember);
    }

    public List<Member> findByUser(User user) {
        return repository.findByUser(user)
                .orElseThrow(this::noMember);
    }

    public List<Member> findByTeam(String teamId) {
        return findByTeam(UUID.fromString(teamId));
    }

    public List<Member> findByTeam(UUID teamId) {
        return repository.findByTeam_TeamId(teamId)
                .orElseThrow(this::noMember);
    }

    public List<Member> findByTeam(Team team) {
        return repository.findByTeam(team)
                .orElseThrow(this::noMember);
    }

    public List<UUID> findTeamIdsByUser(String userId) {
        return findTeamIdsByUser(UUID.fromString(userId));
    }

    public List<UUID> findTeamIdsByUser(UUID userId) {
        return repository.findTeamIdsByUserId(userId);
    }

    private ApplicationException noMember() {
        return new ApplicationException(ErrorCode.NO_SUCH_MEMBER);
    }

    public boolean isMemberOfTeam(String teamId, String userId) {
        return repository.findByTeam_TeamIdAndUser_UserId(
                UUID.fromString(teamId),
                UUID.fromString(userId)
        ).isPresent();
    }
}
