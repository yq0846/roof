package com.side.jiboong.domain.user;

import com.side.jiboong.common.annotation.WriteService;
import com.side.jiboong.common.component.RedisCacheManager;
import com.side.jiboong.common.config.properties.JwtProperties;
import com.side.jiboong.common.exception.UserAlreadyExistsException;
import com.side.jiboong.common.security.JwtProvider;
import com.side.jiboong.common.security.UserAuth;
import com.side.jiboong.domain.user.entity.User;
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

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@WriteService
@RequiredArgsConstructor
public class UserWriteService {

    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;
    private final JwtProvider jwtProvider;
    private final RedisCacheManager redisCacheManager;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final BCryptPasswordEncoder encoder;

    public AuthenticationTokens signIn(SignInCredentials credentials) {
        User user = userRepository.findByUsername(credentials.username())
                .orElseThrow(() -> new NoSuchElementException("회원정보를 찾을 수 없습니다."));

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password());
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(token); // UserDetailsService.loadUserByUsername() 호출
        UserAuth userAuth = (UserAuth) authenticate.getPrincipal();

        return createAuthToken(userAuth, userAuth.getUsername());
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

    public User join(UserJoin userJoin) {
        if (!isValidEmail(userJoin.username())) {
            throw new IllegalArgumentException("invalid email");
        }
        if (!isValidPassword(userJoin.password())) {
            throw new IllegalArgumentException("invalid password");
        }

        userRepository.findByUsername(userJoin.username())
                .ifPresent((user) -> {
                    throw new UserAlreadyExistsException("Username " + user.getUsername() + " already exists.");
                });

        User newUser = userJoin.toUser(encoder::encode);
        userRepository.save(newUser);

        return newUser;
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
