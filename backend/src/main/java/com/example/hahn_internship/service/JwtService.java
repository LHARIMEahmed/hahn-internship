package com.example.hahn_internship.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.hahn_internship.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Service for generating, parsing, and validating JWT tokens.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Returns the signing key used for JWT generation and validation.
     * 
     * @return Key used to sign JWT
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT token for a given user.
     *
     * @param user the user for whom to generate the token
     * @return JWT token as a String
     */
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .claim("userId", user.getId())
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username (email) from a JWT token.
     *
     * @param token the JWT token
     * @return username (email) contained in the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from a JWT token using a resolver function.
     *
     * @param <T> the type of the claim
     * @param token the JWT token
     * @param resolver function to extract the claim from Claims
     * @return extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return resolver.apply(claims);
    }

    /**
     * Checks if a JWT token is valid for a given user.
     *
     * @param token the JWT token
     * @param user the user to validate against
     * @return true if token is valid and belongs to the user, false otherwise
     */
    public boolean isTokenValid(String token, User user) {
        return extractUsername(token).equals(user.getEmail())
                && extractClaim(token, Claims::getExpiration).after(new Date());
    }
}
