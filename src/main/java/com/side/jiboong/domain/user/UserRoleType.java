package com.side.jiboong.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoleType {
    ADMIN("관리자"), USER("일반회원");

    private final String description;
}
