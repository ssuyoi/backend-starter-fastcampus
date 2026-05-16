package com.backendstarter.threadboard.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtService {

    private static final SecretKey key = Jwts.SIG.HS256.key().build();

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername());
    }

    public String getUsername(String accessToken) {
        return getSubject(accessToken);
    }

    private String generateToken(String subject) {
        var now = new Date();
        var exp = new Date(now.getTime() + 1000 * 60 * 60 * 3); //만료시점:3시간
        String jws = Jwts.builder().subject(subject).signWith(key)
            .issuedAt(now)
            .expiration(exp)
            .compact();

        return jws;
    }

    private String getSubject(String token) {
        try {
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload()
                .getSubject();
        } catch (JwtException e) {
            log.error("JwtException", e);
            throw e;
        }
    }
}
