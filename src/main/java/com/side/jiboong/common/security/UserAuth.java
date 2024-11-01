package com.side.jiboong.common.security;

import com.side.jiboong.domain.user.UserRoleType;
import com.side.jiboong.domain.user.entity.User;
import com.side.jiboong.domain.user.entity.UsersAuthorities;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class UserAuth implements UserDetails {
    private final Long userId;

    private final String username;

    private final String password;

    Collection<GrantedAuthority> authorities = new ArrayList<>();

    @Builder
    public UserAuth(
            Long userId,
            String username,
            String password,
            List<UserRoleType> authorities
    ) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        authorities.forEach(roleType -> this.authorities.add((GrantedAuthority) () -> "ROLE_" + roleType.toString()));
    }

    public static UserAuth from(User user) {
        return UserAuth.from(user, Map.of());
    }

    public static UserAuth from(User user, Map<String, Object> attribute) {

        List<UserRoleType> authorities = user.getAuthorities().stream()
                .map(UsersAuthorities::getRoleType).toList();

        return UserAuth.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
