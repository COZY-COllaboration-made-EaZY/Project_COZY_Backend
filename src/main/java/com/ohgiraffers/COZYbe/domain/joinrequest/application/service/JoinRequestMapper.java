package com.ohgiraffers.COZYbe.domain.joinrequest.application.service;

import com.ohgiraffers.COZYbe.domain.joinrequest.application.dto.response.JoinRequestDTO;
import com.ohgiraffers.COZYbe.domain.joinrequest.domain.entity.JoinRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JoinRequestMapper {

    @Mapping(source = "requester.nickname", target = "requesterName")
    @Mapping(source = "team.teamName", target = "teamName")
    JoinRequestDTO entityToDto(JoinRequest joinRequest);

    List<JoinRequestDTO> entityListToDto(List<JoinRequest> joinRequests);
}
