package com.side.jiboong.domain.user.response;

import com.side.jiboong.domain.user.entity.User;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record UserInfo(
        String username,
        ZonedDateTime lastLogin,
        Integer loginCount
) {
    public static UserInfo from(User user) {
        return UserInfo.builder()
                .username(user.getUsername())
                .lastLogin(user.getLastLogin())
                .loginCount(user.getLoginCount())
                .build();
    }
}
