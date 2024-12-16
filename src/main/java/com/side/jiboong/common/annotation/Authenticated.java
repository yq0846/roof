package com.side.jiboong.common.annotation;

import com.side.jiboong.domain.user.UserRoleType;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface Authenticated {
    UserRoleType userType() default UserRoleType.USER;
}
