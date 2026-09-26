package com.bridgelabz.fundoo.notes.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenCacheService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String TOKEN_PREFIX = "auth:token:";

    public void saveToken(String token, String email, long expirationMillis) {

        String key = TOKEN_PREFIX + token;

        redisTemplate.opsForValue().set(
                key,
                email,
                Duration.ofMillis(expirationMillis)
        );
    }

    public boolean isTokenValid(String token) {

        String key = TOKEN_PREFIX + token;

        return Boolean.TRUE.equals(
                redisTemplate.hasKey(key)
        );
    }

    public void removeToken(String token) {

        String key = TOKEN_PREFIX + token;

        redisTemplate.delete(key);
    }
}