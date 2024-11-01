package com.side.jiboong.domain.user.response;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;

import java.time.ZonedDateTime;
import java.util.Set;

@Builder
public record AuthenticationTokens(
        String username,
        String accessToken,
        String refreshToken,
        ZonedDateTime accessTokenExpiresIn,
        ZonedDateTime refreshTokenExpiresIn,
        Set<GrantedAuthority> roles
) {}
