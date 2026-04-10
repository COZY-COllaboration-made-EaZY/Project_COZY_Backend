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
import com.ohgiraffers.COZYbe.domain.user.application.dto.UserSettingsDTO;
import com.ohgiraffers.COZYbe.domain.user.application.dto.UserUpdateDTO;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import com.ohgiraffers.COZYbe.domain.user.domain.service.UserDomainService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import java.time.LocalDateTime;

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
        return toUserInfoDTO(registered);
    }



    public UserInfoDTO getUserInfo(String userId) {
        User user = userDomainService.getUser(userId);
        return toUserInfoDTO(user);
    }

    public UserSettingsDTO getUserSettings(String userId) {
        User user = userDomainService.getUser(userId);
        return new UserSettingsDTO(
                user.getThemeMode(),
                user.getNotificationsEmail(),
                user.getNotificationsPush(),
                user.getDigestWeekly(),
                user.getProfileVisible(),
                user.getLocale()
        );
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
    public UserInfoDTO updateUser(String userId, UserUpdateDTO updateDTO, MultipartFile profileImage) {
        User exist = userDomainService.getUser(userId);
        if (updateDTO.getNickname() != null && !updateDTO.getNickname().isEmpty()){
            exist.setNickname(updateDTO.getNickname());
        }

        if (updateDTO.getStatusMessage() != null){
            exist.setStatusMessage(updateDTO.getStatusMessage());
        }

        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String profileImageUrl = fileService.saveProfileImage(profileImage);
                exist.setProfileImageUrl(profileImageUrl);
            } catch (Exception e) {
                log.error("프로필 이미지 저장 실패", e);
            }
        }
        return toUserInfoDTO(exist);
    }

    @Transactional
    public UserSettingsDTO updateUserSettings(String userId, UserSettingsDTO settingsDTO) {
        User exist = userDomainService.getUser(userId);

        if (settingsDTO.themeMode() != null) {
            exist.setThemeMode(settingsDTO.themeMode());
        }
        if (settingsDTO.notificationsEmail() != null) {
            exist.setNotificationsEmail(settingsDTO.notificationsEmail());
        }
        if (settingsDTO.notificationsPush() != null) {
            exist.setNotificationsPush(settingsDTO.notificationsPush());
        }
        if (settingsDTO.digestWeekly() != null) {
            exist.setDigestWeekly(settingsDTO.digestWeekly());
        }
        if (settingsDTO.profileVisible() != null) {
            exist.setProfileVisible(settingsDTO.profileVisible());
        }
        if (settingsDTO.locale() != null) {
            exist.setLocale(settingsDTO.locale());
        }

        return new UserSettingsDTO(
                exist.getThemeMode(),
                exist.getNotificationsEmail(),
                exist.getNotificationsPush(),
                exist.getDigestWeekly(),
                exist.getProfileVisible(),
                exist.getLocale()
        );
    }

    public AccessInfoDTO verifyUser(LoginDTO dto){
        User user = userDomainService.getUserByEmail(dto.getEmail());
        ensureNotBlocked(user);
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new ApplicationException(ErrorCode.INVALID_PASSWORD);
        }
        return userMapper.EntityToAccessInfoDTO(user);
    }

    public AccessInfoDTO verifyUser(String userId){
        User user = userDomainService.getUser(userId);
        ensureNotBlocked(user);
        return userMapper.EntityToAccessInfoDTO(user);
    }

    @Transactional
    public void updateLastLogin(String userId) {
        User user = userDomainService.getUser(userId);
        user.setLastLoginAt(LocalDateTime.now());
    }


    @Transactional
    public void deleteUser(String userId) {
        List<RefreshToken> refreshTokens = refreshTokenService.findByUserId(userId);
        refreshTokenService.delete(refreshTokens);
        userDomainService.deleteUser(userId);
    }

    private void ensureNotBlocked(User user) {
        if (Boolean.TRUE.equals(user.getBlocked())) {
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
        }
    }

    private UserInfoDTO toUserInfoDTO(User user) {
        UserInfoDTO dto = userMapper.EntityToInfoDTO(user);
        String profileKeyOrUrl = user.getProfileImageUrl();
        String presigned = fileService.getProfileImageUrl(profileKeyOrUrl);
        if (presigned == null) {
            presigned = fileService.getProfileImageUrl(fileService.getDefaultProfileImageDir());
        }
        return new UserInfoDTO(
                dto.userId(),
                dto.email(),
                dto.nickname(),
                presigned,
                dto.statusMessage(),
                dto.role(),
                dto.themeMode(),
                dto.notificationsEmail(),
                dto.notificationsPush(),
                dto.digestWeekly(),
                dto.profileVisible(),
                dto.locale()
        );
    }
}
