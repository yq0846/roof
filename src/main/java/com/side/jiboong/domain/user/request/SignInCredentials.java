package com.side.jiboong.domain.user.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignInCredentials(
        @Schema(example = "test@co.kr", description = "아이디 (이메일 형식)")
        String username,
        @Schema(example = "1234", description = "비밀번호")
        String password
) {}
