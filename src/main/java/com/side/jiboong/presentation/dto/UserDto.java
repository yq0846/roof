package com.side.jiboong.presentation.dto;

import com.side.jiboong.domain.user.request.UserJoin;
import com.side.jiboong.domain.user.response.UserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.ZonedDateTime;

public class UserDto {
    public record EmailInfo(
            @Schema(example = "test@co.kr", description = "유저 이메일")
            String email
    ) {}

    public record EmailVerification(
            String username,
            String verificationCode
    ) {}

    public record Signup(
            String username,
            String password,
            String verificationCode
    ) {
        public UserJoin toUserJoin() {
            return UserJoin.builder()
                    .username(username)
                    .password(password)
                    .verificationCode(verificationCode)
                    .build();
        }
    }

    public record UpdateUserPasswordInfo(
            @Schema(description = "새로운 비밀번호")
            String newPassword
    ) {}

    public record TokenVerification(
            String accessToken
    ) {}

    @Builder
    public record Items(
            String username,
            ZonedDateTime lastLogin,
            Integer loginCount
    ) {
        public static Items from(UserInfo userInfo) {
            return Items.builder()
                    .username(userInfo.username())
                    .lastLogin(userInfo.lastLogin())
                    .loginCount(userInfo.loginCount())
                    .build();
        }
    }
}
