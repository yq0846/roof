package com.side.jiboong.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserDto {
    public record EmailInfo(
            @Schema(example = "test@naver.com", description = "유저 이메일")
            String email
    ) {}

    public record UpdateUserPasswordInfo(
            @Schema(description = "새로운 비밀번호")
            String newPassword
    ) {}
}
