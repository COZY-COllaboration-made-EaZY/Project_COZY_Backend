package com.ohgiraffers.COZYbe.config;

import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.UserRole;
import com.ohgiraffers.COZYbe.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OperatorBootstrap implements ApplicationRunner {

    private final UserRepository userRepository;

    private static final String OPERATOR_NICKNAME = "admin";
    private static final String OPERATOR_EMAIL = "qwe@qwe";

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        String nickname = normalize(OPERATOR_NICKNAME);
        String email = normalize(OPERATOR_EMAIL);

        if (nickname == null || email == null) {
            log.info("Operator bootstrap skipped: missing nickname or email.");
            return;
        }

        Optional<User> target = userRepository.findByEmail(email)
                .filter(user -> nickname.equalsIgnoreCase(user.getNickname()));

        if (target.isEmpty()) {
            target = userRepository.findByNickname(nickname)
                    .filter(user -> email.equalsIgnoreCase(user.getEmail()));
        }

        if (target.isEmpty()) {
            log.info("Operator bootstrap: user not found for email '{}' and nickname '{}'.", email, nickname);
            return;
        }

        User user = target.get();
        if (user.getRole() == UserRole.OPERATOR) {
            log.info("Operator bootstrap: user already operator. userId={}", user.getUserId());
            return;
        }

        user.setRole(UserRole.OPERATOR);
        userRepository.save(user);
        log.info("Operator bootstrap: promoted to operator. userId={}", user.getUserId());
    }

    private String normalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
