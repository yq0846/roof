package com.side.jiboong.common.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.side.jiboong.common.component.RedisCacheManager;
import com.side.jiboong.common.config.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {
    private final JwtProperties jwtProperties;
    private final RedisCacheManager redisCacheManager;

    public String createJwtToken(JwtClaim claim) {
        String roles = claim.roles.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return JWT.create()
                .withIssuer(claim.username())
                .withSubject(jwtProperties.subject())
                .withIssuedAt(new Date())
                .withExpiresAt(ZonedDateTime.now().plus(jwtProperties.accessTokenExpiration()).toInstant())
                .withClaim("username", claim.username())
                .withClaim("role", roles)
                .sign(jwtProperties.algorithm());
    }

    public String createRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public Authentication getAuthentication(String accessToken, Function<String, UserAuth> func) throws AuthenticationException {
        if (!StringUtils.hasText(accessToken)) {
            throw new AuthenticationException("토큰이 제공되지 않았습니다.") {};
        }

        String username = redisCacheManager.getValue(accessToken);

        if (username == null || !Objects.equals(username, this.getUsername(accessToken))) {
            throw new AuthenticationException("유효하지 않거나 만료된 토큰입니다.") {};
        }

        UserAuth userAuth = func.apply(username);

        return new UsernamePasswordAuthenticationToken(
                userAuth,
                "",
                userAuth.getAuthorities()
        );
    }

    public String getUsername(String jwtToken) {
        DecodedJWT decodedJWT = this.decode(jwtToken);
        return decodedJWT.getClaim("username").asString();
    }

    private DecodedJWT decode(String jwtToken) {
        return JWT.require(jwtProperties.algorithm())
                .build()
                .verify(jwtToken);
    }

    public record JwtClaim(
            String username,
            Set<GrantedAuthority> roles
    ) {}
}
