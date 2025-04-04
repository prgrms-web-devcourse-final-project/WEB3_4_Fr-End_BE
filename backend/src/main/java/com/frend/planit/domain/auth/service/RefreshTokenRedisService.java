package com.frend.planit.domain.auth.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenRedisService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String PREFIX = "RT:";

    public void save(Long userId, String refreshToken, long expirationMs) {
        String key = getKey(userId);
        redisTemplate.opsForValue().set(key, refreshToken, Duration.ofMillis(expirationMs));
    }

    public String get(Long userId) {
        return redisTemplate.opsForValue().get(getKey(userId));
    }

    public void delete(Long userId) {
        redisTemplate.delete(getKey(userId));
    }

    private String getKey(Long userId) {
        return PREFIX + userId;
    }
}
