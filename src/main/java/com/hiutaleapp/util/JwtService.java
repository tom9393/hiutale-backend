package com.hiutaleapp.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

    private static final String JWT_SECRET = "8FA0432E035F87DD9A81DAA97047EE0290E21EBA444A8E8FA1B96FB84AE2888B8447B9CE621E6DE5F6FFF27B39814E7CFA4951194062FAAF9A8AE8092F71B152";
    private static final long VALIDITY = TimeUnit.MINUTES.toMillis(60);

    public String generateToken(UserDetails userDetails) {
        Map<String, String> claims = new HashMap<>();
        claims.put("iss", "hiutaleapp");
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(VALIDITY)))
                .signWith(generateKey())
                .compact();
    }

    private SecretKey generateKey() {
        byte[] decodedKey = Base64.getDecoder().decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public String extractId(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public boolean isValid(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
