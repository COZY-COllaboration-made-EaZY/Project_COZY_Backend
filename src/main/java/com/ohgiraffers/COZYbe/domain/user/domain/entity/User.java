package com.ohgiraffers.COZYbe.domain.user.domain.entity;


import com.ohgiraffers.COZYbe.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_user")
public class User extends BaseTimeEntity {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private UUID userId;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "status_message", length = 255)
    private String statusMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20)
    @Builder.Default
    private UserRole role = UserRole.USER;

    @Column(name = "blocked")
    @Builder.Default
    private Boolean blocked = false;

    @Column(name = "theme_mode", length = 20)
    private String themeMode;

    @Column(name = "notifications_email")
    private Boolean notificationsEmail;

    @Column(name = "notifications_push")
    private Boolean notificationsPush;

    @Column(name = "digest_weekly")
    private Boolean digestWeekly;

    @Column(name = "profile_visible")
    private Boolean profileVisible;

    @Column(name = "locale", length = 10)
    private String locale;

    @Column(name = "last_login_at")
    private java.time.LocalDateTime lastLoginAt;

}
