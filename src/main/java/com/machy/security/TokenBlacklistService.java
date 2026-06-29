package com.machy.security;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    private final ConcurrentHashMap<String, Long> blacklist = new ConcurrentHashMap<>();

    public void invalidate(String token) {
        blacklist.put(token, System.currentTimeMillis());
    }

    public boolean isInvalidated(String token) {
        return blacklist.containsKey(token);
    }
}
