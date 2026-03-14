package com.ohgiraffers.COZYbe.domain.admin.application.service;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.admin.application.dto.AdminUserDTO;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.UserRole;
import com.ohgiraffers.COZYbe.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<AdminUserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public AdminUserDTO updateBlocked(String userId, boolean blocked) {
        User user = userRepository.findById(java.util.UUID.fromString(userId))
                .orElseThrow(() -> new ApplicationException(ErrorCode.NO_SUCH_USER));

        if (blocked && user.getRole() == UserRole.OPERATOR) {
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
        }

        user.setBlocked(blocked);
        User saved = userRepository.save(user);
        return toDto(saved);
    }

    private AdminUserDTO toDto(User user) {
        boolean blocked = Boolean.TRUE.equals(user.getBlocked());
        return new AdminUserDTO(
                user.getUserId().toString(),
                user.getEmail(),
                user.getNickname(),
                user.getRole(),
                blocked,
                user.getLastLoginAt()
        );
    }
}
