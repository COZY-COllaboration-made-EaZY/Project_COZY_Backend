package com.ohgiraffers.COZYbe.domain.member.application.service;

import com.ohgiraffers.COZYbe.domain.member.application.dto.response.MemberBriefDTO;
import com.ohgiraffers.COZYbe.domain.member.domain.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.nickname", target = "memberName")
    MemberBriefDTO entityToDto(Member member);

    List<MemberBriefDTO> entityListToDto(List<Member> members);
}
