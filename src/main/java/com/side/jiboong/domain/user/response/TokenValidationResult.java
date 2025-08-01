package com.side.jiboong.domain.user.response;

public record TokenValidationResult(
        Boolean valid,
        String username
) {}
