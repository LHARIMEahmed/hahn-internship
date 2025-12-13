package com.example.hahn_internship.service;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.hahn_internship.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    // Clé secrète simple
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final long expiration = 1000 * 60 * 60; // 1h
    // Génération du token JWT
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .claim("userId", user.getId())
                .signWith(key)
                .compact();
    }
}
