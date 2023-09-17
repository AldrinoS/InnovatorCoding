package com.example.innovatorcodingchallenge.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
@Component
public class JwtGenerator {

    public static final String key = "secret";

    public String generate(Authentication authentication) {
        String username = authentication.getName();
        Instant current = Instant.now();
        Duration duration = Duration.of(700, ChronoUnit.SECONDS);
        Instant future = current.plus(duration);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(current))
                .setExpiration(Date.from(future))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        }catch (Exception e) {
            return false;
        }

        return true;
    }


}
