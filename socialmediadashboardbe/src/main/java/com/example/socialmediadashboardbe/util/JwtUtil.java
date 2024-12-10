package com.example.socialmediadashboardbe.util;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "secret";
    public String generateToken(String username)
    {
        return Jwts.builder()
               .setSubject(username)
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + 3600000))
               .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
               .compact();
    }
    public String extractUsername(String token)
    {
        return Jwts.parser()
               .setSigningKey(SECRET_KEY)
               .parseClaimsJws(token)
               .getBody()
               .getSubject();
    }
    public boolean isTokenValid(String token, String username)
    {
        return extractUsername(token).equals(username)&& !isTokenExpired(token);
    }
    public boolean isTokenExpired(String token)
    {
        return Jwts.parser()
               .setSigningKey(SECRET_KEY)
               .parseClaimsJws(token)
               .getBody()
               .getExpiration().before(new Date());
    }
}
