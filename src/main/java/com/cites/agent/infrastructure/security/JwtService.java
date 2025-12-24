package com.cites.agent.infrastructure.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Service
public final class JwtService {

    private final Key key;
    private final long expiration;

    public JwtService(
        @Value("${security.jwt.secret}") String secret,
        @Value("${security.jwt.expiration}") long expiration
    ) {
        if (secret.length() < 32) {
            throw new IllegalArgumentException(
                "JWT secret must be at least 32 characters"
            );
        }

        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    public String generateToken(UserPrincipal principal) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("tenantId", principal.getTenantId().toString());
        claims.put("role", principal.getRole().name());

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(principal.getUsername()) // email
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
