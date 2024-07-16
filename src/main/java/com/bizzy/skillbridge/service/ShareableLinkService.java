package com.bizzy.skillbridge.service;

import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ShareableLinkService {

    private static final long EXPIRATION_TIME = TimeUnit.HOURS.toMillis(24); // Link valid for 24 hours
    private final Map<String, Long> linkExpiryMap = new HashMap<>();

    public String generateShareableLink(String portfolioId, String itemId) {
        String token = Base64.getEncoder().encodeToString((portfolioId + ":" + itemId + ":" + new Date().getTime()).getBytes());
        linkExpiryMap.put(token, new Date().getTime() + EXPIRATION_TIME);
        return "https://example.com/portfolio/" + portfolioId + "/item/" + itemId + "?token=" + token;
    }

    public boolean isLinkValid(String token) {
        Long expiryTime = linkExpiryMap.get(token);
        return expiryTime != null && new Date().getTime() <= expiryTime;
    }
}
