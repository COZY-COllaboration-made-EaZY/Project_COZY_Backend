package com.ohgiraffers.COZYbe.domain.user.domain.service;

import com.ohgiraffers.COZYbe.common.error.ApplicationException;
import com.ohgiraffers.COZYbe.common.error.ErrorCode;
import com.ohgiraffers.COZYbe.domain.user.domain.entity.User;
import com.ohgiraffers.COZYbe.domain.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class UserDomainService {

    private final UserRepository repository;



    public User saveUser(User user) {
        return repository.save(user);
    }

    public User getUser(String userId) {
        return getUser(UUID.fromString(userId));
    }

    public User getUser(UUID userId) {
        return repository.findById(userId).orElseThrow(this::noUser);
    }

    public User getUserByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(this::noUser);
    }

    public User getUserByNickname(String nickname) {
        return repository.findByNickname(nickname).orElseThrow(this::noUser);
    }

    public User getReference(String userId) {
        return repository.getReferenceById(UUID.fromString(userId));
    }

    public User getReference(UUID userId) {
        return repository.getReferenceById(userId);
    }

    public boolean isEmailExist(String email) {
        return repository.findByEmail(email).isPresent();
    }

    public boolean isNicknameExist(String nickname) {
        return repository.findByNickname(nickname).isPresent();
    }

    public boolean isUserExist(String userId) {
        return isUserExist(UUID.fromString(userId));
    }

    public boolean isUserExist(UUID userId) {
        return repository.existsById(userId);
    }

    public void deleteUser(String userId) {
        deleteUser(UUID.fromString(userId));
    }

    public void deleteUser(UUID userId) {
        User user = getUser(userId);
        repository.delete(user);
    }

    public void deleteUser(User user) {
        repository.delete(user);
    }

    private ApplicationException noUser() {
        return new ApplicationException(ErrorCode.NO_SUCH_USER);
    }
}
