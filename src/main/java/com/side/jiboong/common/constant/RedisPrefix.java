package com.side.jiboong.common.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RedisPrefix {
    ALARM("alarm"),
    ;

    private final String prefix;

    @Override
    public String toString() {
        return prefix;
    }
}
