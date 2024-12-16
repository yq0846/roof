package com.side.jiboong.presentation;

import io.swagger.v3.oas.annotations.media.Schema;


public record ApiResponse<T>(
        @Schema(description = "응답 데이터를 반환합니다.")
        T body,
        @Schema(description = "에러 메시지를 반환합니다.")
        String message
) {
    public static <T> ApiResponse<T> ok(T body) {
        return new ApiResponse<>(body, null);
    }

    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>(null, message);
    }
}
