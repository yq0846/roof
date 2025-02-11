package com.side.jiboong.common.config;

import com.side.jiboong.common.annotation.Authenticated;
import com.side.jiboong.common.exception.UnauthorizedException;
import com.side.jiboong.domain.user.UserRoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationAspect {

    @Before("@annotation(com.side.jiboong.common.annotation.Authenticated)")
    public void checkAuthentication(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Authenticated authenticated = signature.getMethod().getAnnotation(Authenticated.class);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserRoleType requiredUserType = authenticated.userType();

        verify(authentication, requiredUserType);
    }

    private void verify(Authentication authentication, UserRoleType userType) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        if (!hasRequiredAuthority(authentication, UserRoleType.ADMIN) && !hasRequiredAuthority(authentication, userType)) {
            throw new AccessDeniedException(userType.getDescription() + " 접근 권한이 없습니다.");
        }
    }

    private boolean hasRequiredAuthority(Authentication authentication, UserRoleType userType) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + userType.name()));
    }
}
