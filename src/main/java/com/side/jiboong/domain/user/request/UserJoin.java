package com.side.jiboong.domain.user.request;

import com.side.jiboong.domain.user.entity.User;

import java.util.function.Function;

public record UserJoin(
        String username,
        String password
){
    public User toUser(Function<String, String> encoder) {
        return User.builder()
                .username(this.username)
                .password(encoder.apply(this.password))
                .loginCount(0)
                .build();
    }
}
