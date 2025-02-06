package com.side.jiboong.domain.user;

import com.side.jiboong.common.annotation.ReadService;
import com.side.jiboong.common.component.RedisCacheManager;
import com.side.jiboong.common.exception.UserAlreadyExistsException;
import com.side.jiboong.common.security.UserAuth;
import com.side.jiboong.domain.user.entity.User;
import com.side.jiboong.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Objects;

@ReadService
@RequiredArgsConstructor
public class UserReadService {
    private final UserRepository userRepository;
    private final RedisCacheManager redisCacheManager;

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
    }

    public UserAuth getUserAuthByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserAuth::from)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
    }

    public void validateEmailDuplication(String email) {
        if (!StringUtils.hasText(email)) {
            throw new UserAlreadyExistsException("email is invalid or already taken");
        }

        if (userRepository.existsByUsername(email.trim())) {
            throw new UserAlreadyExistsException("email is invalid or already taken");
        }
    }

    public void validateVerificationCode(String email, String verificationCode) {
        String storedVerificationCode = redisCacheManager.getValue("verification-code::" + email);

        if (!Objects.equals(storedVerificationCode, verificationCode)) {
            throw new NoSuchElementException("invalid verification code");
        }

        redisCacheManager.setValue("verification-code::" + email, verificationCode, Duration.ofMinutes(30L));
    }
}
