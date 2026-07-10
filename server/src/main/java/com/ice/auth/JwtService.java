package com.ice.auth;

import com.ice.config.AuthProperties;
import com.ice.domain.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final AuthProperties authProperties;
    private final SecretKey secretKey;

    public JwtService(AuthProperties authProperties) {
        this.authProperties = authProperties;
        this.secretKey = Keys.hmacShaKeyFor(
                authProperties.getJwtSecret().getBytes(StandardCharsets.UTF_8)
        );
    }

    public String issueToken(long userId, byte roleCode) {
        long now = System.currentTimeMillis();
        long exp = now + authProperties.getJwtExpireSeconds() * 1000L;
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", roleCode)
                .issuedAt(new Date(now))
                .expiration(new Date(exp))
                .signWith(secretKey)
                .compact();
    }

    public JwtClaims parseToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        long userId = Long.parseLong(claims.getSubject());
        Object roleClaim = claims.get("role");
        byte roleCode = roleClaim instanceof Number number
                ? number.byteValue()
                : UserRole.USER.code();
        return new JwtClaims(userId, UserRole.fromCode(roleCode));
    }

    public long getExpireSeconds() {
        return authProperties.getJwtExpireSeconds();
    }

    public record JwtClaims(long userId, UserRole role) {
    }
}
