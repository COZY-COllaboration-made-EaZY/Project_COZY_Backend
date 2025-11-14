package com.ohgiraffers.COZYbe.domain.teams.application.service;


import com.ohgiraffers.COZYbe.domain.teams.application.dto.response.TeamDetailDTO;
import com.ohgiraffers.COZYbe.domain.teams.application.dto.response.TeamNameDTO;
import com.ohgiraffers.COZYbe.domain.teams.domain.entity.Team;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeamMapper {

    List<TeamNameDTO> entityListToDto(List<Team> teams);

    @Mapping(source = "leader.nickname", target = "leaderName")
    TeamDetailDTO entityToDetail(Team team);

}
