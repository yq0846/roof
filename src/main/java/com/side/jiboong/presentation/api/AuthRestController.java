package com.side.jiboong.presentation.api;

import com.side.jiboong.domain.user.UserReadService;
import com.side.jiboong.domain.user.UserWriteService;
import com.side.jiboong.domain.user.request.RefreshTokenRequest;
import com.side.jiboong.domain.user.request.SignInCredentials;
import com.side.jiboong.domain.user.response.AuthenticationTokens;
import com.side.jiboong.domain.user.response.TokenValidationResult;
import com.side.jiboong.presentation.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication API", description = "인증 API")
public class AuthRestController {
    private final UserReadService userReadService;
    private final UserWriteService userWriteService;

    @Operation(summary = "회원가입 인증코드 발송", description = """
        이메일로 인증코드를 발송합니다.
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
        인증코드가 올바른지 검증합니다.
    """)
    @PostMapping("/signup/verify-email")
    public ResponseEntity<String> verifyEmail(
            @RequestBody UserDto.EmailVerification emailVerification
    ) {
        userReadService.validateVerificationCode(emailVerification.username(), emailVerification.verificationCode());
        return ResponseEntity.status(HttpStatus.OK).body("Email 인증 성공.");
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = """
        회원가입을 진행합니다.
    """)
    public ResponseEntity<String> signIn(
            @RequestBody UserDto.Signup signup
    ) {
        userWriteService.join(signup.toUserJoin());
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공.");
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
    @Operation(summary = "비밀번호 재설정", description = """
        비밀번호를 재설정하고 DB에 재설정된 비밀번호로 변경합니다.
    """)
    public ResponseEntity<String> resetPassword(
            @RequestParam("resetCode") String resetCode,
            @RequestBody UserDto.UpdateUserPasswordInfo info
    ) {
        userWriteService.resetPassword(resetCode, info.newPassword());
        return ResponseEntity.status(HttpStatus.OK).body("비밀번호 재설정 완료.");
    }

    @PostMapping("/validate-token")
    @Operation(summary = "토큰 검증", description = """
        토큰의 유효성을 검증합니다.
    """)
    public ResponseEntity<TokenValidationResult> validateToken(
            @RequestBody UserDto.TokenVerification token
    ) {
        TokenValidationResult result = userWriteService.validateToken(token.accessToken());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/all-user-list")
    @Operation(summary = "유저 전체 조회")
    public ResponseEntity<List<UserDto.Items>> getAllBy() {
        List<UserDto.Items> users = userReadService.getAllUser().stream()
                .map(UserDto.Items::from)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
