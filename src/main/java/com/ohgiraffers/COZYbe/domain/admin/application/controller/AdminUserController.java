package com.ohgiraffers.COZYbe.domain.admin.application.controller;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.admin.application.dto.AdminUserDTO;
import com.ohgiraffers.COZYbe.domain.admin.application.service.AdminUserService;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.UserRole;
import com.ohgiraffers.COZYbe.domain.user.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final UserDomainService userDomainService;

    @GetMapping
    public List<AdminUserDTO> listUsers(@AuthenticationPrincipal Jwt jwt) {
        requireOperator(jwt);
        return adminUserService.getAllUsers();
    }

    @PatchMapping("/{userId}/block")
    public AdminUserDTO updateBlocked(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String userId,
            @RequestBody Map<String, Boolean> body
    ) {
        requireOperator(jwt);
        Boolean blocked = body.get("blocked");
        if (blocked == null) {
            throw new ApplicationException(ErrorCode.INVALID_USER);
        }
        return adminUserService.updateBlocked(userId, blocked);
    }

    private void requireOperator(Jwt jwt) {
        if (jwt == null) {
            throw new ApplicationException(ErrorCode.ANONYMOUS_USER);
        }
        User user = userDomainService.getUser(jwt.getSubject());
        if (user.getRole() != UserRole.OPERATOR) {
            throw new ApplicationException(ErrorCode.NOT_ALLOWED);
        }
    }
}
