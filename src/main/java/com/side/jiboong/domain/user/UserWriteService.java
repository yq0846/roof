package com.side.jiboong.domain.user;

import com.side.jiboong.common.annotation.WriteService;
import com.side.jiboong.common.component.MailSender;
import com.side.jiboong.common.component.RedisCacheManager;
import com.side.jiboong.common.config.properties.ExpirationProperties;
import com.side.jiboong.common.config.properties.JwtProperties;
import com.side.jiboong.common.exception.*;
import com.side.jiboong.common.security.JwtProvider;
import com.side.jiboong.common.security.UserAuth;
import com.side.jiboong.domain.user.entity.User;
import com.side.jiboong.domain.user.request.RefreshTokenRequest;
import com.side.jiboong.domain.user.request.SignInCredentials;
import com.side.jiboong.domain.user.request.UserJoin;
import com.side.jiboong.domain.user.response.AuthenticationTokens;
import com.side.jiboong.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.*;

@WriteService
@RequiredArgsConstructor
public class UserWriteService {

    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;
    private final JwtProvider jwtProvider;
    private final RedisCacheManager redisCacheManager;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final BCryptPasswordEncoder encoder;
    private final MailSender mailSender;
    private final ExpirationProperties expirationProperties;

    public User join(UserJoin userJoin) {
        if (!isValidEmail(userJoin.username())) {
            throw new IllegalArgumentException("이메일이 유효하지 않습니다.");
        }
        if (!isValidPassword(userJoin.password())) {
            throw new IllegalArgumentException("비밀번호가 유효하지 않습니다.");
        }

        userRepository.findByUsername(userJoin.username())
                .ifPresent((user) -> {
                    throw new UserAlreadyExistsException("이메일: " + user.getUsername() + " 이미 가입된 사용자입니다.");
                });

        User newUser = userJoin.toUser(encoder::encode);
        userRepository.save(newUser);

        deleteVerificationCode(userJoin.username(), userJoin.verificationCode());

        return newUser;
    }

    public AuthenticationTokens signIn(SignInCredentials credentials) {
        userRepository.findByUsername(credentials.username())
                .orElseThrow(UserNotFoundException::new);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password());
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(token); // UserDetailsService.loadUserByUsername() 호출
        UserAuth userAuth = (UserAuth) authenticate.getPrincipal();

        return createAuthToken(userAuth, userAuth.getUsername());
    }

    public void sendPasswordResetCode(String username) {

        userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        String resetCode = generateResetCode(username);

        redisCacheManager.setValue(resetCode, username, expirationProperties.emailVerification());

        mailSender.send(
                username,
                "Password Reset Code",
                resetCode
        );
    }

    public void resetPassword(String resetCode, String newPassword) {
        String email = redisCacheManager.getValue(resetCode);
        if (!StringUtils.hasText(email)) {
            throw new InvalidResetCodeException("비밀번호 재설정 코드가 유효하지 않습니다.");
        }

        redisCacheManager.deleteValue(resetCode);

        User findUser = userRepository.findByUsername(email)
                .orElseThrow(UserNotFoundException::new);

        String encodedPassword = encoder.encode(newPassword);

        findUser.resetPassword(encodedPassword);
        userRepository.save(findUser);
    }

    public AuthenticationTokens refreshToken(RefreshTokenRequest request) {
        if(!StringUtils.hasText(request.refreshToken())) {
            throw new UnauthorizedException("Refresh token 을 입력바랍니다.");
        }

        String storedUsername = redisCacheManager.getValue(request.refreshToken());

        if(storedUsername == null) {
            throw new UnauthorizedException("유효하지 않거나 만료된 Refresh token 토큰입니다.");
        }

        User user = userRepository.findByUsername(storedUsername)
                .orElseThrow(UserNotFoundException::new);
        UserAuth userAuth = UserAuth.from(user);

        return createAuthToken(userAuth, user.getUsername());
    }

    private AuthenticationTokens createAuthToken(UserAuth userAuth, String username) {
        Set<GrantedAuthority> roles = new HashSet<>(userAuth.getAuthorities());

        JwtProvider.JwtClaim jwtClaim = new JwtProvider.JwtClaim(username, roles);
        String accessToken = jwtProvider.createJwtToken(jwtClaim);
        String refreshToken = jwtProvider.createRefreshToken();

        redisCacheManager.setValue(accessToken, username, jwtProperties.accessTokenExpiration());
        redisCacheManager.setValue(refreshToken, username, jwtProperties.refreshTokenExpiration());

        ZonedDateTime accessTokenExpiresIn = ZonedDateTime.now().plus(jwtProperties.accessTokenExpiration());
        ZonedDateTime refreshTokenExpiresIn = ZonedDateTime.now().plus(jwtProperties.refreshTokenExpiration());

        return AuthenticationTokens.builder()
                .username(userAuth.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(accessTokenExpiresIn)
                .refreshTokenExpiresIn(refreshTokenExpiresIn)
                .roles(roles)
                .build();
    }

    public void sendVerificationCode(String email) {
        String verificationCode = generateVerificationCode();

        redisCacheManager.setValue(
                "verification-code::" + email,
                verificationCode,
                expirationProperties.emailVerification()
        );

        mailSender.send(
                email,
                "Verification Code",
                verificationCode
        );
    }

    private void deleteVerificationCode(String email, String verificationCode) {
        String storedVerificationCode = redisCacheManager.getValue("verification-code::" + email);

        if (!Objects.equals(storedVerificationCode, verificationCode)) {
            throw new InvalidVerificationCodeException("유효하지 않거나 만료된 인증코드입니다.");
        }

        redisCacheManager.deleteValue("verification-code::" + email);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int number = random.nextInt(1000000); // 0부터 999999까지의 난수 생성
        return String.format("%06d", number); // 6자리로 포맷팅
    }

    private String generateResetCode(String email) {
        UUID randomUUID = UUID.randomUUID();
        String input = randomUUID + email;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] hash = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));

            return convertToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String convertToString(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte bt : hash) {
            String hex = Integer.toHexString(0xff & bt);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static boolean isValidEmail(String username) {
        for (char c : username.toCharArray()) {
            if (c < 33 || c > 127) { // 32: Space, 128: ASCII Extended
                return false;
            }
        }

        return username.contains("@")
                && username.contains(".")
                && !username.contains(";")
                && !username.contains("--")
                && username.length() > 6
                && username.length() < 321
                ;
    }

    private static boolean isValidPassword(String password) {
        boolean containsUpperCase = false;
        boolean containsLowerCase = false;

        for (char c : password.toCharArray()) {
            if (c < 33 || c > 127) { // 32: Space, 128: ASCII Extended
                return false;
            }
            if (Character.isUpperCase(c)) {
                containsUpperCase = true;
            }
            if (Character.isLowerCase(c)) {
                containsLowerCase = true;
            }
        }

        return password.length() > 7
                && containsUpperCase
                && containsLowerCase
                ;
    }
}
