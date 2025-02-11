package com.side.jiboong.common.constant;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisKey {
    POSTING_ALARM(RedisPrefix.ALARM, "POSTING_ALARM"),
    ;

    private final RedisPrefix prefix;
    private final String keyName;

    @Override
    public String toString() { //prefix:KEY
        return String.format("%s:%s", prefix, this.keyName);
    }
}