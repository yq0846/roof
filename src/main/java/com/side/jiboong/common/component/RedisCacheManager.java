package com.side.jiboong.common.component;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisCacheManager {

    private final RedisTemplate<String, Object> redisTemplate;

    public <T> T getValue(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public void setValue(String key, String value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }
}
