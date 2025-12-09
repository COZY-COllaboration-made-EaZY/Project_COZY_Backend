package com.ohgiraffers.COZYbe.domain.user.application.service;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.auth.application.dto.AccessInfoDTO;
import com.ohgiraffers.COZYbe.domain.auth.application.dto.LoginDTO;
import com.ohgiraffers.COZYbe.domain.auth.domain.entity.RefreshToken;
import com.ohgiraffers.COZYbe.domain.auth.domain.service.RefreshTokenService;
import com.ohgiraffers.COZYbe.domain.files.service.FileService;
import com.ohgiraffers.COZYbe.domain.user.application.dto.SignUpDTO;
import com.ohgiraffers.COZYbe.domain.user.application.dto.UserInfoDTO;
import com.ohgiraffers.COZYbe.domain.user.application.dto.UserUpdateDTO;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import com.ohgiraffers.COZYbe.domain.user.domain.service.UserDomainService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserAppService {

    private final UserDomainService userDomainService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    private final RefreshTokenService refreshTokenService;
    private final FileService fileService;


    @Transactional
    public UserInfoDTO registerDefault(SignUpDTO signUpDTO) {
        if (!isEmailAvailable(signUpDTO.getEmail())){
            throw new ApplicationException(ErrorCode.INVALID_EMAIL);
        };

        String profileImageUrl = fileService.getDefaultProfileImageDir();
        User user = User.builder()
                .email(signUpDTO.getEmail())
                .nickname(signUpDTO.getNickname())
                .password(passwordEncoder.encode(signUpDTO.getPassword()))
                .profileImageUrl(profileImageUrl)
                .statusMessage(signUpDTO.getStatusMessage())
                .build();

        User registered = userDomainService.saveUser(user);
        return userMapper.EntityToInfoDTO(registered);
    }



    public UserInfoDTO getUserInfo(String userId) {
        User user = userDomainService.getUser(userId);
        return userMapper.EntityToInfoDTO(user);
    }

    public boolean isEmailAvailable(String email) {
        return !userDomainService.isEmailExist(email);
    }

    public Boolean verifyPassword(String userId, String inputPassword) {
        User user = userDomainService.getUser(userId);
        Boolean isMatched = passwordEncoder.matches(inputPassword, user.getPassword());
        if (!isMatched){
            log.warn("verifying password failed : {}", user.getEmail());
        }
        return isMatched;
    }


    @Transactional
    public UserInfoDTO updateUser(String userId, UserUpdateDTO updateDTO) {
        User exist = userDomainService.getUser(userId);
        if (updateDTO.getNickname() != null && !updateDTO.getNickname().isEmpty()){
            exist.setNickname(updateDTO.getNickname());
        }

        if (updateDTO.getNickname() != null && !updateDTO.getNickname().isEmpty()){
            exist.setStatusMessage(updateDTO.getStatusMessage());
        }
        return userMapper.EntityToInfoDTO(exist);
    }

    public AccessInfoDTO verifyUser(LoginDTO dto){
        User user = userDomainService.getUserByEmail(dto.getEmail());
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }
        return userMapper.EntityToAccessInfoDTO(user);
    }

    public AccessInfoDTO verifyUser(String userId){
        User user = userDomainService.getUser(userId);
        return userMapper.EntityToAccessInfoDTO(user);
    }


    @Transactional
    public void deleteUser(String userId) {
        List<RefreshToken> refreshTokens = refreshTokenService.findByUserId(userId);
        refreshTokenService.delete(refreshTokens);
        userDomainService.deleteUser(userId);
    }
}
