package com.side.jiboong.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final AuthenticationProvider authenticationProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws IOException, ServletException {
        String tokenHeader = request.getHeader("Authorization");
        try {
            if (StringUtils.startsWithIgnoreCase(tokenHeader, "Bearer ")) {
                String accessToken = tokenHeader.split(" ")[1]; // Bearer 뒤에 있는 토큰을 가져옴
                Authentication authentication = authenticationProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {

        } finally {
            filterChain.doFilter(request, response);
        }
    }

    @FunctionalInterface
    public interface AuthenticationProvider {
        Authentication getAuthentication(String accessToken) throws AuthenticationException;
    }
}
