package com.side.jiboong.domain.user;

import com.side.jiboong.common.annotation.ReadService;
import com.side.jiboong.common.component.RedisCacheManager;
import com.side.jiboong.common.exception.InvalidVerificationCodeException;
import com.side.jiboong.common.exception.MailSendException;
import com.side.jiboong.common.exception.UserAlreadyExistsException;
import com.side.jiboong.common.exception.UserNotFoundException;
import com.side.jiboong.common.security.UserAuth;
import com.side.jiboong.domain.user.entity.User;
import com.side.jiboong.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@ReadService
@RequiredArgsConstructor
public class UserReadService {
    private final UserRepository userRepository;
    private final RedisCacheManager redisCacheManager;

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    public UserAuth getUserAuthByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserAuth::from)
                .orElseThrow(UserNotFoundException::new);
    }

    public List<Long> getAllUserIdList() {
        return userRepository.findAll().stream()
                .map(User::getId)
                .toList();
    }

    public void validateEmailDuplication(String email) {
        if (!StringUtils.hasText(email)) {
            throw new MailSendException("이메일이 유효하지 않습니다.");
        }

        if (userRepository.existsByUsername(email.trim())) {
            throw new UserAlreadyExistsException();
        }
    }

    public void validateVerificationCode(String email, String verificationCode) {
        String storedVerificationCode = redisCacheManager.getValue("verification-code::" + email);

        if (!Objects.equals(storedVerificationCode, verificationCode)) {
            throw new InvalidVerificationCodeException("유효하지 않은 인증코드입니다.");
        }

        redisCacheManager.setValue("verification-code::" + email, verificationCode, Duration.ofMinutes(30L));
    }
}
