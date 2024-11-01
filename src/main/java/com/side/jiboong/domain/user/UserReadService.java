package com.side.jiboong.domain.user;

import com.side.jiboong.common.annotation.ReadService;
import com.side.jiboong.common.security.UserAuth;
import com.side.jiboong.domain.user.entity.User;
import com.side.jiboong.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;

@ReadService
@RequiredArgsConstructor
public class UserReadService {
    private final UserRepository userRepository;

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
    }

    public UserAuth getUserAuthByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserAuth::from)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
    }
}
