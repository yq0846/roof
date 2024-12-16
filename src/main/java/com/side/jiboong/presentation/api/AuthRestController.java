package com.side.jiboong.presentation.api;

import com.side.jiboong.domain.user.UserWriteService;
import com.side.jiboong.domain.user.request.SignInCredentials;
import com.side.jiboong.domain.user.request.UserJoin;
import com.side.jiboong.domain.user.response.AuthenticationTokens;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication API", description = "인증 API")
public class AuthRestController {
    private final UserWriteService userWriteService;

    @PostMapping("/sign-in")
    @Operation(summary = "JWT 로그인", description = """
        로그인을 하고 AccessToken 과 RefreshToken 을 발급받습니다.
    """)
    public ResponseEntity<AuthenticationTokens> signIn(@RequestBody SignInCredentials credentials) {
        AuthenticationTokens tokens = userWriteService.signIn(credentials);
        return ResponseEntity.status(HttpStatus.OK).body(tokens);
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
}
