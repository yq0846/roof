package com.side.jiboong.presentation.dto;

import com.side.jiboong.domain.user.request.UserJoin;
import io.swagger.v3.oas.annotations.media.Schema;

public class UserDto {
    public record EmailInfo(
            @Schema(example = "test@naver.com", description = "유저 이메일")
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
}
