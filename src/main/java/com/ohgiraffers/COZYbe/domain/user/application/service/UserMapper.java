package com.ohgiraffers.COZYbe.domain.user.application.service;


import com.ohgiraffers.COZYbe.domain.auth.dto.AccessInfoDTO;
import com.ohgiraffers.COZYbe.domain.user.application.dto.UserInfoDTO;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    AccessInfoDTO EntityToAccessInfoDTO(User user);

    UserInfoDTO EntityToInfoDTO(User user);
}
