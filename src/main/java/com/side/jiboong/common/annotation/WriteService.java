package com.side.jiboong.common.annotation;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Component
@Transactional
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface WriteService {
}
