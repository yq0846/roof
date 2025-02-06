package com.side.jiboong.presentation.api;

import com.side.jiboong.domain.user.UserReadService;
import com.side.jiboong.domain.user.UserWriteService;
import com.side.jiboong.domain.user.request.RefreshTokenRequest;
import com.side.jiboong.domain.user.request.SignInCredentials;
import com.side.jiboong.domain.user.request.UserJoin;
import com.side.jiboong.domain.user.response.AuthenticationTokens;
import com.side.jiboong.presentation.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication API", description = "인증 API")
public class AuthRestController {
    private final UserReadService userReadService;
    private final UserWriteService userWriteService;

    @Operation(summary = "회원가입 인증코드 발송", description = """
        이메일로 인증코드를 발송합니다,
    """)
    @PostMapping("/signup/send-email")
    public ResponseEntity<Void> sendEmail(
            @RequestBody UserDto.EmailInfo emailInfo
    ) {
        userReadService.validateEmailDuplication(emailInfo.email());
        userWriteService.sendVerificationCode(emailInfo.email());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "회원가입 인증코드 검증", description = """
        인증코드가 올바른지 검증합니다,
    """)
    @PostMapping("/signup/verify-email")
    public ResponseEntity<String> verifyEmail(
            @RequestBody UserDto.EmailVerification emailVerification
    ) {
        userReadService.validateVerificationCode(emailVerification.username(), emailVerification.verificationCode());
        return ResponseEntity.status(HttpStatus.OK).body("Email verified successfully");
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = """
        회원가입을 진행합니다.
    """)
    public ResponseEntity<String> signIn(
            @RequestBody UserJoin signup
    ) {
        userWriteService.join(signup);
        return ResponseEntity.status(HttpStatus.CREATED).body("User signed up successfully");
    }

    @PostMapping("/sign-in")
    @Operation(summary = "JWT 로그인", description = """
        AccessToken 과 RefreshToken 을 발급받습니다.
    """)
    public ResponseEntity<AuthenticationTokens> signIn(
            @RequestBody SignInCredentials credentials
    ) {
        AuthenticationTokens tokens = userWriteService.signIn(credentials);
        return ResponseEntity.status(HttpStatus.OK).body(tokens);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "토큰 갱신", description = """
        RefreshToken 을 이용해 AccessToken 을 갱신합니다.
    """)
    public ResponseEntity<AuthenticationTokens> refreshToken(
            @RequestBody RefreshTokenRequest refreshTokenRequest
    ) {
        var signInResponse = userWriteService.refreshToken(refreshTokenRequest);
        return ResponseEntity.status(HttpStatus.OK).body(signInResponse);
    }

    @PostMapping("/send-password-reset-code")
    @Operation(summary = "비밀번호 재설정 코드 전송", description = """
        이메일로 비밀번호 재설정 코드를 전송합니다.
    """)
    public ResponseEntity<Void> sendPasswordResetCode(
            @RequestBody UserDto.EmailInfo emailInfo
    ) {
        userWriteService.sendPasswordResetCode(emailInfo.email());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/reset-password")
    @Operation(summary = "비밀번호 재설정", description = "비밀번호를 재설정하고 DB에 재설정된 비밀번호로 변경합니다.")
    public ResponseEntity<String> resetPassword(
            @RequestParam("resetCode") String resetCode,
            @RequestBody UserDto.UpdateUserPasswordInfo info
    ) {
        userWriteService.resetPassword(resetCode, info.newPassword());
        return ResponseEntity.status(HttpStatus.OK).body("Password reset is complete.");
    }
}
