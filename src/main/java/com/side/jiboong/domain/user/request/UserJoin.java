package com.side.jiboong.domain.user.request;

import com.side.jiboong.domain.user.entity.User;
import lombok.Builder;

import java.util.function.Function;

@Builder
public record UserJoin(
        String username,
        String password,
        String verificationCode
){
    public User toUser(Function<String, String> encoder) {
        return User.builder()
                .username(this.username)
                .password(encoder.apply(this.password))
                .loginCount(0)
                .build();
    }
}
